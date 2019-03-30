package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

/**
 * Allows use to accept of reject lend requests
 *
 * @author Kaya Thiessen
 * @author ArtLimbaga
 * @author Devon Deweert
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends AppCompatActivity {
    public final static int PIN_PICKUP_LOCATION = 600;
    public final static String REQUEST_INFORMATION = "RequestInformation";
    // ratio in relation to the original display
    private final Double HEIGHT_RATIO = 0.5;

    private BookRequest mBookRequest;

    private DatabaseHelper mDatabaseHelper;
    private boolean mDatabaseFetchDone = false;

    private TextView mTextViewRequesterUsername;
    private TextView mTextViewRequesterEmail;
    private TextView mTextViewBookTitle;
    private TextView mTextViewBookAuthor;

    private TextView mTextViewBookISBN;

    private ImageView mImageViewBookPhoto;
    private ImageView mImageProfilePhoto;

    private Button mButtonAccept;
    private Button mButtonDecline;

    private Geolocation mPickupLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_lend);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout((int) width, (int) (height * HEIGHT_RATIO));

        mDatabaseHelper = new DatabaseHelper();
        getDefaultPickupLocation();

        Intent intent = getIntent();
        mBookRequest = (BookRequest) intent.getSerializableExtra(REQUEST_INFORMATION);

        mTextViewRequesterUsername = findViewById(R.id.textViewAcceptRejectLendUserName);
        mTextViewRequesterEmail = findViewById(R.id.textViewAcceptRejectLendEmail);
        mTextViewBookTitle = findViewById(R.id.textViewAcceptRejectLendBookTitle);
        mTextViewBookAuthor = findViewById(R.id.textViewAcceptRejectLendBookAuthor);
        mTextViewBookISBN = findViewById(R.id.textViewAcceptRejectLendBookIsbn);

        mImageProfilePhoto = findViewById(R.id.imageViewAcceptRejectLendUserPhoto);
        mImageViewBookPhoto = findViewById(R.id.imageViewAcceptRejectLendBookPhoto);

        setAllViews();

        mButtonAccept = findViewById(R.id.buttonAcceptRejectLendAccept);
        mButtonDecline = findViewById(R.id.buttonAcceptRejectLendDecline);

        mButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSettingLocationDialog();
            }
        });

        mButtonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestUpdate(BookRequestStatus.DENIED);
                finish();
            }
        });

        ConstraintLayout userConstrainLayout = findViewById(R.id.constraintLayoutAcceptRejectLendRequester);

        userConstrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptRejectLendActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookRequest.getBorrower().getUserName());
                startActivity(intent);
            }
        });


    }

    private void setAllViews() {
        UserInformation borrower = mBookRequest.getBorrower();
        mTextViewRequesterUsername.setText(borrower.getUserName());
        mTextViewRequesterEmail.setText(borrower.getEmail());
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

    private void sendRequestUpdate(final BookRequestStatus status) {
        mBookRequest.setCurrentStatus(status);
        mBookRequest.setPickuplocation(mPickupLocation);
        mDatabaseHelper.updateLendRequest(mBookRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    if (status == BookRequestStatus.DENIED) {
                        doDeniedProcess();
                    } else {
                        doAcceptedProcess();
                    }
                } else {
                    ToastMessage.show(getApplicationContext(), "Something went wrong updating the request");
                }
            }
        });
    }

    private void doDeniedProcess() {
        mDatabaseHelper.getSpecificBookLendRequests(mBookRequest.getBookRequested().getOwner(),
                mBookRequest.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if (bookRequestList != null && bookRequestList.size() > 0) {
                            boolean noOpenRequests = true;
                            for (BookRequest bookRequest : bookRequestList.getBookRequests()) {
                                if (bookRequest.getCurrentStatus() != BookRequestStatus.DENIED) {
                                    noOpenRequests = false;
                                }
                            }
                            if (noOpenRequests) {
                                BookInformation bookInformation = mBookRequest.getBookRequested();
                                bookInformation.setStatus(BookStatus.AVAILABLE);
                                mDatabaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            ToastMessage.show(getApplicationContext(), "Request updated successfully");
                                        } else {
                                            ToastMessage.show(getApplicationContext(), "Couldn't update book status to AVAILABLE");
                                        }
                                    }
                                });
                            }
                        } else if (bookRequestList == null) {
                            ToastMessage.show(getApplicationContext(), "Something went wrong updating the book status");
                        } else {
                            ToastMessage.show(getApplicationContext(), "You shouldn't ever see this message...");
                        }
                    }
                });
    }

    private void doAcceptedProcess() {
        mDatabaseHelper.getSpecificBookLendRequests(mBookRequest.getBookRequested().getOwner(),
                mBookRequest.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if (bookRequestList != null && bookRequestList.size() > 0) {
                            for (BookRequest bookRequest : bookRequestList.getBookRequests()) {
                                if (bookRequest.getBookRequestLendKey().equals(mBookRequest.getBookRequestLendKey())) {
                                    continue;
                                }
                                bookRequest.setCurrentStatus(BookRequestStatus.DENIED);
                                mDatabaseHelper.updateLendRequest(bookRequest, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            ToastMessage.show(getApplicationContext(), "Request updated successfully");
                                        } else {
                                            ToastMessage.show(getApplicationContext(), "Something went wrong updating all other requests");
                                        }
                                    }
                                });
                            }
                        } else if (bookRequestList == null) {
                            ToastMessage.show(getApplicationContext(), "Something went wrong updating the book status");
                        } else {
                            ToastMessage.show(getApplicationContext(), "You shouldn't ever see this message...");
                        }
                    }
                });
        BookInformation bookInformation = mBookRequest.getBookRequested();
        bookInformation.setStatus(BookStatus.ACCEPTED);
        mDatabaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    ToastMessage.show(getApplicationContext(), "Book status updated successfully");
                } else {
                    ToastMessage.show(getApplicationContext(), "Book status wasn't updated");
                }
            }
        });
    }


    public void doSettingLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AcceptRejectLendActivity.this);
        builder.setMessage("Use Default Pickup Location?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mDatabaseFetchDone && mPickupLocation != null) {
                            sendRequestUpdate(BookRequestStatus.ACCEPTED);
                        } else if (mDatabaseFetchDone) {
                            ToastMessage.show(AcceptRejectLendActivity.this, "Something went wrong communicating with database");
                        }
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(AcceptRejectLendActivity.this, SelectLocationActivity.class);
                        startActivityForResult(intent, PIN_PICKUP_LOCATION);
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Question:");
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PIN_PICKUP_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                // the coordinates to do whatever you need to do with them
                mPickupLocation.setLatitude(Double.parseDouble(data.getStringExtra("lat")));
                mPickupLocation.setLongitude(Double.parseDouble(data.getStringExtra("lng")));
                sendRequestUpdate(BookRequestStatus.ACCEPTED);
                finish();
            }
        }

    }


    private void getDefaultPickupLocation() {
        mDatabaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                mDatabaseFetchDone = true;
                if (user != null) {
                    mPickupLocation = user.getPickUpLocation();
                }
            }
        });
    }
}

