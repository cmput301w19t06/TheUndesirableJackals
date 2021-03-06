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
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

/**
 * Displays book requests to owner when the borrower scans a borrowed book for return.
 * @author Art Limbaga
 */
public class ViewReturningLendRequestActivity extends AppCompatActivity {

    public final static String RETURNING_REQUEST = "ReturningRequest";
    public final static int BARCODE_SCANNER = 5000;

    private final static String ACTIVITY_TAG = "ViewReturningLend";
    private BookRequest mBookRequest;

    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.6;

    private DatabaseHelper mDatabaseHelper;

    private TextView mTextViewBorrowerUsername;
    private TextView mTextViewBorrowerEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;
    private TextView mTextViewBookISBN;
    private TextView mTextViewScannedISBN;
    private TextView mTextViewUserRole;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    private Button mButtonScanISBN;
    private Button mButtonReceiveBook;

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
        mBookRequest = (BookRequest) intent.getSerializableExtra(RETURNING_REQUEST);

        mTextViewBorrowerUsername = findViewById(R.id.textViewBookRequestHandoffUserName);
        mTextViewBorrowerEmail = findViewById(R.id.textViewBookRequestHandoffEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestHandoffBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestHandoffBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestHandoffBookIsbn);
        mTextViewScannedISBN = findViewById(R.id.textViewBookRequestHandoffScannedISBN);
        mTextViewUserRole = findViewById(R.id.textViewBookRequestHandoffUserRole);

        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestHandoffUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestHandoffBookPhoto);

        setAllViews();

        mButtonScanISBN = findViewById(R.id.buttonBookRequestHandoffScanISBN);
        mButtonReceiveBook = findViewById(R.id.buttonBookRequestHandoffConfirm);
        mButtonReceiveBook.setText("Receive Book");

        mLinearLayoutViewPickup = findViewById(R.id.linearLayoutBookRequestHandoffLocation);

        mButtonScanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewReturningLendRequestActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_SCANNER);
            }
        });

        mButtonReceiveBook.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(ViewReturningLendRequestActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBorrower().getUserName());
                startActivity(intent);
            }
        });
    }

    /**
     * Launches ViewPickUpLocationActivity to display pickup location of the requested book
     */
    private void doViewPickupLocation() {
        if(mBookRequest.getPickuplocation() == null) {
            ToastMessage.show(getApplicationContext(), "Book owner done messed up and accepted your request without setting a location... ");
            return;
        }
        Intent intent = new Intent(ViewReturningLendRequestActivity.this, ViewPickupLocationActivity.class);
        intent.putExtra(ViewPickupLocationActivity.PICKUP_LOCATION, mBookRequest.getPickuplocation());
        startActivity(intent);
    }

    /**
     * Sets all the views for the layout of this activity
     */
    private void setAllViews() {
        mTextViewUserRole.setText("Borrower: ");
        UserInformation borrower = mBookRequest.getBorrower();
        mTextViewBorrowerUsername.setText(borrower.getUserName());
        mTextViewBorrowerEmail.setText(borrower.getEmail());
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
                }
            }
        });

        loadUserPhoto();
    }

    /**
     * Accesses the database to load the borrower's photo into the activity's view
     */
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

    /**
     * Gets called when receive button is pressed.
     */
    private void  doConfirmReceive() {

        if(mTextViewScannedISBN.getText().toString().isEmpty()) {
            ToastMessage.show(getApplicationContext(), "Please scan barcode for ISBN");
            return;
        }

        if(mTextViewScannedISBN.getText().toString().equals(mBookRequest.getBookRequested().getIsbn())) {
            doReceiveUpdates();
        } else {
            ToastMessage.show(getApplicationContext(),"Scanned barcode does not match requested book ISBN");
        }
    }

    /**
     * Runs when receiving a book is confirmed to valid
     */
    private void doReceiveUpdates(){
        deleteBookRequestStatus();
        updateBookInformationStatus();
        finish();
    }

    /**
     * Once a book has been returned to the owner, the requests will be purged from existance
     * byt this function to indicate the end of a book request
     */
    private void deleteBookRequestStatus() {
        mDatabaseHelper.deleteRequest(mBookRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool) {
                    ToastMessage.show(getApplicationContext(), "Book is returned, and book request is deleted.");
                } else {
                    ToastMessage.show(getApplicationContext(), "Something happened, check your network connection and try again");
                }
            }
        });
    }

    /**
     * Updates the book information of the book to indicate that it is available for borrowing
     */
    private void updateBookInformationStatus() {
        mBookRequest.getBookRequested().setStatus(BookStatus.AVAILABLE);
        mDatabaseHelper.updateBookInformation(mBookRequest.getBookRequested(), new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    ToastMessage.show(getApplicationContext(),"You book is now available for borrowing");
                }else {
                    ToastMessage.show(getApplicationContext(), "Something happened, check your network connection and try again");
                }
            }
        });
    }
}
