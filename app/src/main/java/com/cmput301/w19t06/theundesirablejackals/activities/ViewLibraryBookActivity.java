package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.io.File;


/**
 * A general book view of a book displayed on the library tab of the app. Whoever is viewing the
 * book will be able to request the book, go to book owner profile, and view all the images
 * related to the book.
 * @author Art Limbaga
 */
public class ViewLibraryBookActivity extends AppCompatActivity {
    private final static String ACTIVITY_TAG = "ViewLibraryBookActivity";
    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";

    public final static String LIBRARY_BOOK_FROM_RECYCLER_VIEW = "LibraryBookFromRecyclerView";
    public final static String LIBRARY_INFO_FROM_RECYCLER_VIEW = "LibraryBookInfoFromRecyclerView";

    private Toolbar mToolbar;

    private DatabaseHelper mDatabaseHelper;

    private BookInformation mBookInformation;
    private Book mLibraryBook;

    private boolean isFavourite;
    private boolean toggleFavourite;

    private ImageView mBookPhotoView;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mStatus;
    private TextView mBookOwner;
    private TextView mDescription;
    private TextView mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_library_book);

        Button buttonLibraryBookBorrowBook = (Button) findViewById(R.id.buttonLibraryBookBorrowBook);
        buttonLibraryBookBorrowBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAllBorrowRequest();
            }
        });
        Intent intent = getIntent();
        mLibraryBook = (Book) intent.getSerializableExtra(LIBRARY_BOOK_FROM_RECYCLER_VIEW);
        mBookInformation = (BookInformation) intent.getSerializableExtra(LIBRARY_INFO_FROM_RECYCLER_VIEW);

        mToolbar = findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Library Book");
        setSupportActionBar(mToolbar);

        mDatabaseHelper = new DatabaseHelper();

        mBookPhotoView = findViewById(R.id.imageViewViewLibraryBookPhoto);
        mTitle = findViewById(R.id.textViewViewLibraryBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewLibraryBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewLibraryBookBookISBN);
        mStatus = findViewById(R.id.textViewViewLibraryBookBookStatus);
        mBookOwner = findViewById(R.id.textViewViewLibraryBookBookOwner);
        mCategory = findViewById(R.id.textViewViewLibraryBookCategory);
        mDescription = findViewById(R.id.textViewViewLibraryBookBookDescription);

        mTitle.setText(mLibraryBook.getTitle());
        mAuthor.setText(mLibraryBook.getAuthor());
        mIsbn.setText("ISBN: " + mLibraryBook.getIsbn());
        mStatus.setText(mBookInformation.getStatus().toString());
        mBookOwner.setText("Owner: " + mBookInformation.getOwner());
        mCategory.setText("Category: "+ mLibraryBook.getCategories());
        mDescription.setText(mBookInformation.getDescription());

        setStatusTextViewColor();

        setBookPhotoView();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBookOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 
                Intent intent = new Intent(ViewLibraryBookActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookInformation.getOwner());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_others_book, menu);
        setFavouriteIcon(menu.findItem(R.id.itemMenuOthersBookViewFavorite));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // switch case just in case we add another option
        if (id == R.id.itemMenuOthersBookViewFavorite) {
            toggleFavouriteIcon(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFavouriteIcon(MenuItem item) {
        toggleFavourite = !toggleFavourite;
        if(toggleFavourite) {
            item.setIcon(R.drawable.ic_is_favorite);
        } else {
            item.setIcon(R.drawable.ic_action_add_favorite);
        }
    }

    private void setFavouriteIcon(final MenuItem item) {
        Log.d(ACTIVITY_TAG, item.toString());

        mDatabaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                BookToInformationMap favouriteBooks = user.getFavouriteBooks();
                if (favouriteBooks != null) {
                    if (favouriteBooks.contains(mLibraryBook)) {
                        item.setIcon(R.drawable.ic_is_favorite);
                        isFavourite = true;
                        toggleFavourite = true;
                    } else {
                        item.setIcon(R.drawable.ic_action_add_favorite);
                        isFavourite = false;
                        toggleFavourite = false;
                    }
                }

            }
        });
    }


    private void setBookPhotoView() {
        // display download image

        if (mBookInformation.getBookPhoto() != null && !mBookInformation.getBookPhoto().isEmpty()) {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File image = new File(storageDir, mBookInformation.getBookPhoto() + ".jpg");
            if(!image.exists()) {
                mBookPhotoView.setImageResource(R.drawable.ic_loading_with_text);
                try {
                    mDatabaseHelper.downloadBookPicture(image, mBookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if(bool){
                                if(image.exists()){
                                    Uri photoData = Uri.fromFile(image);
                                    mBookPhotoView.setImageURI(photoData);
                                }else{
                                    ToastMessage.show(getApplicationContext(),"Something went quite wrong...");
                                }
                            }else{
                                ToastMessage.show(getApplicationContext(), "Photo not downloaded");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(ERROR_TAG_LOAD_IMAGE, e.getMessage());
                }
            }else{
                Uri photoData = Uri.fromFile(image);
                mBookPhotoView.setImageURI(photoData);
            }
        }
    }
    private void setStatusTextViewColor() {
        switch (mBookInformation.getStatus()) {
            case REQUESTED:
                mStatus.setTextColor(Color.parseColor("#EA4335"));
                break;
            case ACCEPTED:
                mStatus.setTextColor(Color.parseColor("#4285F4"));
                break;
            case BORROWED:
                mStatus.setTextColor(Color.parseColor("#A52A2A"));
                break;
            case AVAILABLE:
                mStatus.setTextColor(Color.parseColor("#34A853"));
                break;
        }
    }

    private void doAllBorrowRequest(){
        mDatabaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
            @Override
            public void onCallback(final UserInformation userInformation) {
                if(userInformation != null){
                    mDatabaseHelper.getBorrowRequests(userInformation.getUserName(), new BookRequestListCallback() {
                        @Override
                        public void onCallback(BookRequestList bookRequestList) {
                            if(bookRequestList != null){
                                if(bookRequestList.size() > 0){
                                    Boolean requestExists = false;
                                    for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                        String currentRequestKey = bookRequest.getBookRequested().getBookInformationKey();
                                        String newRequestKey = mBookInformation.getBookInformationKey();
                                        if(currentRequestKey.equals(newRequestKey)){
                                            requestExists = true;
                                        }
                                    }if(!requestExists){
                                        makeNewBorrowRequest(userInformation);
                                    }else{
                                        ToastMessage.show(getBaseContext(), "You already requested this book");
                                    }
                                }else{
                                    makeNewBorrowRequest(userInformation);
                                }
                            }else{
                                ToastMessage.show(getBaseContext(), "Something went wrong getting your current requests");
                            }
                        }
                    });

                }else{
                    ToastMessage.show(getBaseContext(), "Something happened while fetching your user data");
                }
            }
        });
    }

    private void makeNewBorrowRequest(UserInformation userInformation){
        BookRequest bookRequest = new BookRequest(userInformation, mBookInformation);

        // check if the book requested is a book owned by the current user
        if (!userInformation.getUserName().equals(mBookInformation.getOwner())) {

            mDatabaseHelper.makeBorrowRequest(bookRequest, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if(bool) {
                        ToastMessage.show(getBaseContext(), "Request has been sent to owner");
                    }else{
                        ToastMessage.show(getBaseContext(), "Request not sent correctly");
                    }
                }
            });
        } else {
            ToastMessage.show(ViewLibraryBookActivity.this, "Can't request a book that you own");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateFavouriteBooks();
    }

    private void updateFavouriteBooks() {
        if(isFavourite && !toggleFavourite) {
            deleteFavourite();
        }
        if(!isFavourite && toggleFavourite) {
            addFavourite();
        }
    }

    private void deleteFavourite() {
        mDatabaseHelper.deleteFavouriteBook(mBookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    Log.d(ACTIVITY_TAG, "Book deleted from favourites");
                } else {
                    Log.d(ACTIVITY_TAG, "Book delete from favourites failed");
                }

            }
        });
    }

    private void addFavourite() {
        mDatabaseHelper.addFavouriteBook(mBookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    Log.d(ACTIVITY_TAG, "Book added to favourites");
                } else {
                    Log.d(ACTIVITY_TAG, "Book add to favourites failed");
                }
            }
        });
    }

}
