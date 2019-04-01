package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.io.File;

/**
 * Allows the user to view an owned book and do certain action that only book owners can do.
 * These  actions include editing information about the book, viewing a list of request on the book,
 * Delete the book from owned book list.
 *
 * @author Art Limbaga
 */
public class ViewOwnedBookActivity extends AppCompatActivity {
    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";
    private final static String ACTIVITY_TAG = "ViewOwnedBookActivity";

    public final static int EDIT_BOOK = 51;

    public final static String OWNED_BOOK_FROM_RECYCLER_VIEW = "OwnedBookFromRecyclerView";
    public final static String OWNED_INFO_FROM_RECYCLER_VIEW = "InformationFromRecyclerView";
    public final static String LOGGED_IN_USER = "LoggedInUser";

    private Toolbar mToolbar;
    private DatabaseHelper databaseHelper;

    private Book mOwnedBook;
    private BookInformation mBookInformation;


    private boolean isFavourite;
    private boolean toggleFavourite;

    private ImageView mBookPhotoView;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mStatus;
    private TextView mCategory;
    private TextView mDescription;

    private User mLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);
        mToolbar = findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Owned Book");
        setSupportActionBar(mToolbar);

        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        mOwnedBook = (Book) intent.getSerializableExtra(OWNED_BOOK_FROM_RECYCLER_VIEW);
        mBookInformation = (BookInformation) intent.getSerializableExtra(OWNED_INFO_FROM_RECYCLER_VIEW);
        mLoggedInUser = (User) intent.getSerializableExtra(LOGGED_IN_USER);

        mBookPhotoView = findViewById(R.id.imageViewViewOwnedBookPhoto);
        mTitle = findViewById(R.id.textViewViewOwnedBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewOwnedBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewOwnedBookBookISBN);
        mStatus = findViewById(R.id.textViewViewOwnedBookBookStatus);
        mCategory = findViewById(R.id.textViewViewOwnedBookCategory);
        mDescription = findViewById(R.id.textViewViewOwnedBookBookDescription);

        setStatusTextViewColor();

        mTitle.setText(mOwnedBook.getTitle());
        mAuthor.setText(mOwnedBook.getAuthor());
        mIsbn.setText("ISBN: " + mOwnedBook.getIsbn());
        mStatus.setText(mBookInformation.getStatus().toString());
        mDescription.setText(mBookInformation.getDescription());
        mCategory.setText("Category: "+ mOwnedBook.getCategories());

        setBookPhotoView();


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_owned_book, menu);
        setFavouriteIcon(menu.findItem(R.id.itemMenuOwnedBookViewFavorite));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.itemMenuOwnedBookViewFavorite:
                toggleFavouriteIcon(item);
                break;
            case R.id.itemMenuOwnedBookEdit:
                editBook();
                break;
            case R.id.itemMenuOwnedBookViewRequests:
                doViewRequests();
                break;
            case R.id.itemMenuOwnedBookDelete:
                deleteBook();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private void  doViewRequests() {
        databaseHelper.getLendRequests(mLoggedInUser.getUserInfo().getUserName(), new BookRequestListCallback() {
            @Override
            public void onCallback(BookRequestList bookRequestList) {
                if(bookRequestList!=null) {
                    if (!bookRequestList.contains(mOwnedBook.getIsbn())) {
                        ToastMessage.show(getApplicationContext(), "No Request for this book");
                    } else {
                        showRequests();
                    }
                }
            }
        });
    }

    private void showRequests() {
        Intent intent = new Intent(ViewOwnedBookActivity.this, LentListActivity.class);
        intent.putExtra(LentListActivity.SEARCH_BY_ISBN, mOwnedBook.getIsbn());
        startActivity(intent);
    }

    private void setFavouriteIcon(final MenuItem item) {
        Log.d(ACTIVITY_TAG, item.toString());

        if (mLoggedInUser.getFavouriteBooks() != null) {
            if (mLoggedInUser.getFavouriteBooks().contains(mOwnedBook)) {
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

    private void toggleFavouriteIcon(MenuItem item) {
        toggleFavourite = !toggleFavourite;
        if(toggleFavourite) {
            item.setIcon(R.drawable.ic_is_favorite);
        } else {
            item.setIcon(R.drawable.ic_action_add_favorite);
        }
    }

    private void deleteBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOwnedBookActivity.this);
        builder.setMessage("Deleting this book will send it to oblivion, never to be found again. " +
                "Do You wish to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.deleteOwnedBook(mBookInformation, new BooleanCallback() {
                            @Override
                            public void onCallback(boolean bool) {
                                Intent intent = new Intent(getApplicationContext(), MainHomeViewActivity.class);
                                startActivity(intent);
                                finish();
                                ToastMessage.show(getApplicationContext(), "Book deleted");
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Warning!");
        alert.show();

    }


    private void editBook() {
        Intent intent = new Intent(ViewOwnedBookActivity.this, EditOwnedBookActivity.class);
        intent.putExtra(EditOwnedBookActivity.EDIT_BOOK_OBJECT, mOwnedBook);
        intent.putExtra(EditOwnedBookActivity.EDIT_BOOK_INFO, mBookInformation);
        startActivityForResult(intent, EDIT_BOOK);
    }


    private void setBookPhotoView() {

        if (mBookInformation.getBookPhoto() != null && !mBookInformation.getBookPhoto().isEmpty()) {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File image = new File(storageDir, mBookInformation.getBookPhoto() + ".jpg");
            if (!image.exists()) {
                mBookPhotoView.setImageResource(R.drawable.ic_loading_with_text);
                try {
                    databaseHelper.downloadBookPicture(image, mBookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if (bool) {
                                if (image.exists()) {
                                    Uri photoData = Uri.fromFile(image);
                                    mBookPhotoView.setImageURI(photoData);
                                    Log.d(ACTIVITY_TAG, "image now exists... COOL");
                                } else {
                                    ToastMessage.show(getApplicationContext(), "Something went quite wrong...");
                                }
                            } else {
                                ToastMessage.show(getApplicationContext(), "Photo not downloaded");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(ERROR_TAG_LOAD_IMAGE, e.getMessage());
                }
            } else {
                Uri photoData = Uri.fromFile(image);
                mBookPhotoView.setImageURI(photoData);
                Log.d("ViewBookActivity", "image already exists... COOL");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_BOOK) {
            if (resultCode == RESULT_OK) {
                mDescription.setText(data.getStringExtra("description"));
                Uri imageURI = data.getData();
                if (imageURI != null) {
                    mBookPhotoView.setImageURI(imageURI);
                } else {
                    mBookPhotoView.setImageResource(R.drawable.ic_book);
                }
            }
        } else {
            Log.d(ACTIVITY_TAG, "Unrecognized request code");
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
        databaseHelper.deleteFavouriteBook(mBookInformation, new BooleanCallback() {
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
        databaseHelper.addFavouriteBook(mBookInformation, new BooleanCallback() {
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
