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
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;
/**
 * When book is selected from Borrowed Book request when accepted of currently borrowed. Works for Handoff and pick up of book
 *
 * @author Kaya Thiessen
 * @author Devon Deweert
 */

public class ViewBorrowedBookRequestActivity extends AppCompatActivity {

    public final static String BORROWED_REQUEST = "HandedOffRequest";
    public final static int BARCODE_SCANNER = 3000;

    private final static String ACTIVITY_TAG = "ViewBorrowedBookRequest";
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
    private TextView mTextViewUserRole;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    private Button mButtonScanISBN;
    private Button mButtonReturnBook;

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
        mBookRequest = (BookRequest) intent.getSerializableExtra(BORROWED_REQUEST);

        mTextViewOwnerUsername = findViewById(R.id.textViewBookRequestHandoffUserName);
        mTextViewOwnerEmail = findViewById(R.id.textViewBookRequestHandoffEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestHandoffBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestHandoffBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestHandoffBookIsbn);
        mTextViewScannedISBN = findViewById(R.id.textViewBookRequestHandoffScannedISBN);
        mTextViewUserRole = findViewById(R.id.textViewBookRequestHandoffUserRole);


        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestHandoffUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestHandoffBookPhoto);

        setAllViews();

        mButtonScanISBN = findViewById(R.id.buttonBookRequestHandoffScanISBN);
        mButtonReturnBook = findViewById(R.id.buttonBookRequestHandoffConfirm);
        mButtonReturnBook.setText("Return Book");

        mLinearLayoutViewPickup = findViewById(R.id.linearLayoutBookRequestHandoffLocation);

        mButtonScanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBorrowedBookRequestActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_SCANNER);
            }
        });

        mButtonReturnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReturnBook();
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
                Intent intent = new Intent(ViewBorrowedBookRequestActivity.this, OthersProfileActivity.class);
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
        Intent intent = new Intent(ViewBorrowedBookRequestActivity.this, ViewPickupLocationActivity.class);
        intent.putExtra(ViewPickupLocationActivity.PICKUP_LOCATION, mBookRequest.getPickuplocation());
        startActivity(intent);
    }


    private void setAllViews() {
        mTextViewUserRole.setText("Owner: ");
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

    private void doReturnBook() {

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
        updateBookRequestStatus();
        updateCurrentUserBorrowedBooks();
        finish();
    }

    private void updateBookRequestStatus() {
        mBookRequest.setCurrentStatus(BookRequestStatus.RETURNING);
        mDatabaseHelper.updateLendRequest(mBookRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    ToastMessage.show(getApplicationContext(), "Book is now being returned");
                } else {
                    ToastMessage.show(getApplicationContext(), "Something happened, check your network connection and try again");
                }
            }
        });

    }


    private void updateCurrentUserBorrowedBooks() {
        mDatabaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                BookToInformationMap borrowedBooks = user.getBorrowedBooks();
                borrowedBooks.deleteBook(mBookRequest.getBookRequested().getIsbn());
                mDatabaseHelper.updateBorrowedBooks(borrowedBooks, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if (bool) {
                            ToastMessage.show(getApplicationContext(), "Book deleted from your borrowed list");
                        } else {
                            ToastMessage.show(getApplicationContext(), "Failed to update borrowed books in database");
                        }
                    }
                });
            }
        });
    }
}
