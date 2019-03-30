package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class ViewBookRequestInfoAsBorrowerActivity extends AppCompatActivity {
    public final static String AS_BORROWER_VIEW_BOOK_REQUEST_INFO = "AsBorrowerViewBookRequestInfo";
    private BookRequest mBookRequest;

    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.5;

    private DatabaseHelper mDatabaseHelper;

    private TextView mTextViewOwnerUsername;
    private TextView mTextViewOwnerEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;
    private TextView mTextViewBookISBN;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;


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

        Intent intent = getIntent();
        mBookRequest = (BookRequest) intent.getSerializableExtra(AS_BORROWER_VIEW_BOOK_REQUEST_INFO);

        mTextViewOwnerUsername = findViewById(R.id.textViewBookRequestInfoUserName);
        mTextViewOwnerEmail = findViewById(R.id.textViewBookRequestInfoEmail);
        mTextViewBookTitle = findViewById(R.id.textViewBookRequestInfoBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewBookRequestInfoBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewBookRequestInfoBookIsbn);

        mImageProfilePhoto = findViewById(R.id.imageViewBookRequestInfoUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewBookRequestInfoBookPhoto);


        setAllViews();

        ConstraintLayout borrowerConstraintLayout = findViewById(R.id.constraintLayoutBookRequestInfoUserRole);
        borrowerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ViewBookRequestInfoAsBorrowerActivity.this,
                        OthersProfileActivity.class);

                intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBookRequested().getOwner());
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
                ViewBookRequestInfoAsBorrowerActivity.this,
                ViewPickupLocationActivity.class);

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
        });

    }

}
