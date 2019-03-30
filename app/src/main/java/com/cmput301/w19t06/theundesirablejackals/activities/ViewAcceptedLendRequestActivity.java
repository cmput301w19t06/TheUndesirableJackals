package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class ViewAcceptedLendRequestActivity extends AppCompatActivity {
    public final static String ACCEPTED_REQUEST = "AcceptedRequest";
    public final static int BARCODE_SCANNER = 1000;

    private final static String ACTIVITY_TAG = "ViewAcceptedBookRequest";
    private BookRequest mBookRequest;

    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.6;

    private DatabaseHelper mDatabaseHelper;

    private TextView mTextViewBorrowerUsername;
    private TextView mTextViewBorowerEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;
    private TextView mTextViewBookISBN;
    private TextView mTextViewScannedISBN;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    private Button mButtonScanISBN;
    private Button mButtonConfirmHandoff;

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
        mBookRequest = (BookRequest) intent.getSerializableExtra(ACCEPTED_REQUEST);

        mTextViewBorrowerUsername = findViewById(R.id.textViewBookRequestHandoffUserName);
        mTextViewBorowerEmail = findViewById(R.id.textViewBookRequestHandoffEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestHandoffBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestHandoffBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestHandoffBookIsbn);
        mTextViewScannedISBN = findViewById(R.id.textViewBookRequestHandoffScannedISBN);

        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestHandoffUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestHandoffBookPhoto);

        setAllViews();

        mButtonScanISBN = findViewById(R.id.buttonBookRequestHandoffScanISBN);
        mButtonConfirmHandoff = findViewById(R.id.buttonBookRequestHandoffConfirm);

        mLinearLayoutViewPickup = findViewById(R.id.linearLayoutBookRequestHandoffLocation);

        mButtonScanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAcceptedLendRequestActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_SCANNER);
            }
        });

        mButtonConfirmHandoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConfirmHandOff();
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
                Intent intent = new Intent(ViewAcceptedLendRequestActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBorrower().getUserName());
                startActivity(intent);
            }
        });
    }

    private void doViewPickupLocation() {
        if(mBookRequest.getPickuplocation() == null) {
            ToastMessage.show(getApplicationContext(), "Book owner done messed up and accepted your request without setting a location... ");
            return;
        }
        Intent intent = new Intent(ViewAcceptedLendRequestActivity.this, ViewPickupLocationActivity.class);
        intent.putExtra(ViewPickupLocationActivity.PICKUP_LOCATION, mBookRequest.getPickuplocation());
        startActivity(intent);
    }


    private void setAllViews() {
        UserInformation borrower = mBookRequest.getBorrower();
        mTextViewBorrowerUsername.setText(borrower.getUserName());
        mTextViewBorowerEmail.setText(borrower.getEmail());
        mDatabaseHelper.getBookFromDatabase(mBookRequest.getBookRequested().getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                mTextViewBookAuthor.setText(book.getAuthor());
                mTextViewBookTitle.setText(book.getTitle());
                mTextViewBookISBN.setText("ISBN: " + book.getIsbn());

                if (book.getThumbnail() != null  && !book.getThumbnail().isEmpty()) {
                    Picasso.get()
                            .load(book.getThumbnail())
                            .error(R.drawable.book_icon)
                            .placeholder(R.drawable.book_icon)
                            .into(mImageViewBookPhoto);
                }else{
                    Picasso.get()
                            .load(R.drawable.book_icon)
                            .into(mImageViewBookPhoto);
                }
            }
        });

        loadUserPhoto();
    }

    private void loadUserPhoto() {
        if (mBookRequest.getBorrower().getUserPhoto() != null
                && !mBookRequest.getBorrower().getUserPhoto().isEmpty()) {
            mDatabaseHelper.getProfilePictureUri(mBookRequest.getBorrower(), new UriCallback() {
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

    private void  doConfirmHandOff() {

        if(mTextViewScannedISBN.getText().toString().isEmpty()) {
            ToastMessage.show(getApplicationContext(), "Please scan barcode for ISBN");
            return;
        }

        if(mTextViewScannedISBN.getText().toString().equals(mBookRequest.getBookRequested().getIsbn())) {
            doHandOffUpdates();
        } else {
            ToastMessage.show(getApplicationContext(),"Scanned barcode does not match requested book ISBN");
        }
    }

    private void doHandOffUpdates(){
        mBookRequest.setCurrentStatus(BookRequestStatus.HANDED_OFF);
        mDatabaseHelper.updateLendRequest(mBookRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    ToastMessage.show(getApplicationContext(), "Book has been handed off, please give the book to the requester");
                }else{
                    ToastMessage.show(getApplicationContext(), "Something happened, check your network connection and try again");
                }
            }
        });
    }
}
