package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class ViewBookRequestInfo extends AppCompatActivity {
    public final static String BOOK_REQUEST_INFO = "BookRequestInfo";
    public final static String VIEW_REQUEST_AS = "ViewRequestAs";
    private BookRequest mBookRequest;

    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.5;

    private DatabaseHelper mDatabaseHelper;
    private BroadcastReceiver currentActivityReceiver;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;
    private TextView mTextViewBookISBN;
    private TextView mTextViewUserRole;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    public enum ViewRequestInfoAs {
        BORROWER,
        OWNER,
    }

    private ViewRequestInfoAs mViewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request_info_view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout(width, (int) (height * HEIGHT_RATIO));

        mDatabaseHelper = new DatabaseHelper();
        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        Intent intent = getIntent();
        mBookRequest = (BookRequest) intent.getSerializableExtra(BOOK_REQUEST_INFO);
        mViewMode = (ViewRequestInfoAs) intent.getSerializableExtra(VIEW_REQUEST_AS);


        mTextViewUsername = findViewById(R.id.textViewBookRequestInfoUserName);
        mTextViewEmail = findViewById(R.id.textViewBookRequestInfoEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestInfoBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestInfoBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestInfoBookIsbn);
        mTextViewUserRole = findViewById(R.id.textViewBookRequestInfoUserRole);

        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestInfoUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestInfoBookPhoto);

        if (mViewMode.equals(ViewRequestInfoAs.OWNER)) {
            setAllViewsAsOwner();
        } else {
            setAllViewsAsBorrower();
        }


        ConstraintLayout borrowerConstraintLayout = findViewById(R.id.constraintLayoutBookRequestInfoUserRole);

        borrowerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ViewBookRequestInfo.this,
                        OthersProfileActivity.class);

                if (mViewMode.equals(ViewRequestInfoAs.OWNER)) {
                    intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBorrower().getUserName());
                } else {
                    intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBookRequested().getOwner());
                }

                startActivity(intent);
            }
        });

        LinearLayout viewPickLocationLayout = findViewById(R.id.linearLayoutBookRequestInfoLocation);
        viewPickLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doViewPickupLocation();
            }
        });

    }

    private void doViewPickupLocation() {
        if (mBookRequest.getPickuplocation() == null) {
            ToastMessage.show(getApplicationContext(), "Book owner done messed up and accepted your request without setting a location... ");
            return;
        }
        Intent intent = new Intent(
                ViewBookRequestInfo.this,
                ViewPickupLocationActivity.class);

        intent.putExtra(ViewPickupLocationActivity.PICKUP_LOCATION, mBookRequest.getPickuplocation());
        startActivity(intent);
    }


    private void setAllViewsAsBorrower() {
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

    private void setAllViewsAsOwner() {
        mTextViewUserRole.setText("Borrower: ");
        UserInformation borrower = mBookRequest.getBorrower();
        mTextViewUsername.setText(borrower.getUserName());
        mTextViewEmail.setText(borrower.getEmail());
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
            }
            }
        });

        loadBorrowerPhoto();
    }

    private void loadOwnerInformation() {

        mDatabaseHelper.getUserInfoFromDatabase
                (mBookRequest.getBookRequested().getOwner(), new UserInformationCallback() {
                    @Override
                    public void onCallback(UserInformation userInformation) {
                    mTextViewUsername.setText(userInformation.getUserName());
                    mTextViewEmail.setText(userInformation.getEmail());
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

    private void loadBorrowerPhoto() {
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

    public void messageReceivedRefresh(){
        mDatabaseHelper.getRequest(mBookRequest.getBorrower().getUserName(), mBookRequest.getBookRequestBorrowKey(), new BookRequestCallback() {
            @Override
            public void onCallback(BookRequest bookRequest) {
                if(bookRequest != null && bookRequest.getCurrentStatus() == BookRequestStatus.HANDED_OFF){
                    finish();
                    Intent intent = new Intent(ViewBookRequestInfo.this, ViewHandedoffBookRequestActivity.class);
                    intent.putExtra(ViewHandedoffBookRequestActivity.HANDED_OFF_REQUEST, bookRequest);
                    startActivity(intent);

                }else if(bookRequest != null && bookRequest.getCurrentStatus() == BookRequestStatus.RETURNING) {
                    Intent intent = new Intent(ViewBookRequestInfo.this, ViewReturningLendRequestAcitivity.class);
                    intent.putExtra(ViewHandedoffBookRequestActivity.HANDED_OFF_REQUEST, bookRequest);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(currentActivityReceiver);
        currentActivityReceiver = null;
        super.onPause();
    }

    @Override
    protected void onStop(){
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(currentActivityReceiver);
        currentActivityReceiver = null;
        super.onStop();
    }

}
