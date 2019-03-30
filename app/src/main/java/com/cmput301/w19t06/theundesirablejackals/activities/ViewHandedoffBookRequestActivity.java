package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewHandedoffBookRequestActivity extends AppCompatActivity {
    public final static String HANDED_OFF_REQUEST = "HandedOffRequest";
    public final static int BARCODE_SCANNER = 2000;

    private final static String ACTIVITY_TAG = "ViewHandedoffBookReq";
    private BookRequest mBookRequest;

    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.6;

    private DatabaseHelper mDatabaseHelper;

    private TextView mTextViewOwnerUsername;
    private TextView mTextViewOwnerEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;
    private TextView mTextViewBookISBN;
    private TextView mTextViewScannedISBN;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    private Button mButtonScanISBN;
    private Button mButtonRecieveBook;

    private LinearLayout mLinearLayoutViewPickup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request_handoff);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout(width, (int) (height * HEIGHT_RATIO));

        mDatabaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        mBookRequest = (BookRequest) intent.getSerializableExtra(HANDED_OFF_REQUEST);

        mTextViewOwnerUsername = findViewById(R.id.textViewBookRequestHandoffUserName);
        mTextViewOwnerEmail = findViewById(R.id.textViewBookRequestHandoffEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestHandoffBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestHandoffBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestHandoffBookIsbn);
        mTextViewScannedISBN = findViewById(R.id.textViewBookRequestHandoffScannedISBN);

        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestHandoffUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestHandoffBookPhoto);

        setAllViews();

        mButtonScanISBN = findViewById(R.id.buttonBookRequestHandoffScanISBN);
        mButtonRecieveBook = findViewById(R.id.buttonBookRequestHandoffConfirm);
        mButtonRecieveBook.setText("Receive Book");

        mLinearLayoutViewPickup = findViewById(R.id.linearLayoutBookRequestHandoffLocation);

        mButtonScanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHandedoffBookRequestActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_SCANNER);
            }
        });

        mButtonRecieveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConfirmReceive();
            }
        });

        mLinearLayoutViewPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doViewPickupLocation();
            }
        });

        ConstraintLayout borrowerConstraintLayout = findViewById(R.id.constraintLayoutBookRequestHandoffUserRole);
        borrowerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHandedoffBookRequestActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBookRequested().getOwner());
                startActivity(intent);
            }
        });
    }

    private void doViewPickupLocation() {
        if (mBookRequest.getPickuplocation() == null) {
            ToastMessage.show(getApplicationContext(), "Book owner done messed up and accepted your request without setting a location... ");
            return;
        }
        Intent intent = new Intent(ViewHandedoffBookRequestActivity.this, ViewPickupLocationActivity.class);
        intent.putExtra(ViewPickupLocationActivity.PICKUP_LOCATION, mBookRequest.getPickuplocation());
        startActivity(intent);
    }


    private void setAllViews() {
        mDatabaseHelper.getBookFromDatabase(mBookRequest.getBookRequested().getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                mTextViewBookAuthor.setText(book.getAuthor());
                mTextViewBookTitle.setText(book.getTitle());
                mTextViewBookISBN.setText("ISBN: " + book.getIsbn());

                if (book.getThumbnail() != null && !book.getThumbnail().isEmpty()) {
                    Picasso.get()
                            .load(book.getThumbnail())
                            .error(R.drawable.book_icon)
                            .placeholder(R.drawable.book_icon)
                            .into(mImageViewBookPhoto);
                } else {
                    Picasso.get()
                            .load(R.drawable.book_icon)
                            .into(mImageViewBookPhoto);
                }
            }
        });

        loadOwnerInformation();
    }

    private void loadOwnerInformation() {
        mDatabaseHelper.getUserInfoFromDatabase
            (mBookRequest.getBookRequested().getOwner(), new UserInformationCallback() {
                @Override
                public void onCallback(UserInformation userInformation) {
                    mTextViewOwnerUsername.setText(userInformation.getUserName());
                    mTextViewOwnerEmail.setText(userInformation.getEmail());
                    if (userInformation.getUserPhoto() != null
                            && !userInformation.getUserPhoto().isEmpty()) {
                        mDatabaseHelper.getProfilePictureUri(userInformation, new UriCallback() {
                            @Override
                            public void onCallback(Uri uri) {
                                if (uri != null) {
                                    Picasso.get()
                                            .load(uri)
                                            .error(R.drawable.ic_person_outline_grey_24dp)
                                            .placeholder(R.drawable.ic_loading_with_text)
                                            .into(mImageProfilePhoto);
                                }
                            }
                        });
                    }

                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_SCANNER) {
            if (resultCode == RESULT_OK) {
                mTextViewScannedISBN.setText(data.getStringExtra("ISBN"));
                ToastMessage.show(getApplicationContext(), "ISBN scanned");
            } else {
                ToastMessage.show(getApplicationContext(), "No results found");
            }
        } else {
            Log.d(ACTIVITY_TAG, "Unrecognized request code");
        }
    }

    private void doConfirmReceive() {

        if (mTextViewScannedISBN.getText().toString().isEmpty()) {
            ToastMessage.show(getApplicationContext(), "Please scan barcode for ISBN");
            return;
        }

        if (mTextViewScannedISBN.getText().toString().equals(mBookRequest.getBookRequested().getIsbn())) {
            doRequestUpdate();
        } else {
            ToastMessage.show(getApplicationContext(), "Scanned barcode does not match requested book ISBN");
        }
    }

    private void doRequestUpdate() {
        ToastMessage.show(ViewHandedoffBookRequestActivity.this, "DEVON, INSERT WORK HERE");
//        mBookRequest.setCurrentStatus(BookRequestStatus.HANDED_OFF);
//        mDatabaseHelper.updateLendRequest(mBookRequest, new BooleanCallback() {
//            @Override
//            public void onCallback(boolean bool) {
//                if (bool) {
//                    ToastMessage.show(getApplicationContext(), "Book has been handed off, please give the book to the requester");
//                } else {
//                    ToastMessage.show(getApplicationContext(), "Something happened, check your network connection and try again");
//                }
//            }
//        });
    }
}