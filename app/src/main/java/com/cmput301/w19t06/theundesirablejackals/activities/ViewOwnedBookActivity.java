package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookPhotoUrlCallBack;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

/**
 * Allows the user to view an owned book and do certain action that only book owners can do.
 * These  actions include editing information about the book, viewing a list of request on the book,
 * Delete the book from owned book list.
 * @author Art Limbaga
 */
public class ViewOwnedBookActivity extends AppCompatActivity {
    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";

    public final static String OWNED_BOOK_FROM_RECYCLER_VIEW = "OwnedBookFromRecyclerView";
    public final static String OWNED_INFO_FROM_RECYCLER_VIEW = "InformationFromRecyclerView";

    private Toolbar mToolbar;
    private DatabaseHelper databaseHelper;

    private Book mOwnedBook;
    private BookInformation mBookInformation;

    private ImageView mBookPhotoView;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mStatus;
    private TextView mDescription;

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

        mBookPhotoView = findViewById(R.id.imageViewViewOwnedBookPhoto);
        mTitle = findViewById(R.id.textViewViewOwnedBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewOwnedBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewOwnedBookBookISBN);
        mStatus = findViewById(R.id.textViewViewOwnedBookBookStatus);
        mDescription = findViewById(R.id.textViewViewOwnedBookBookDescription);

        mTitle.setText(mOwnedBook.getTitle());
        mAuthor.setText(mOwnedBook.getAuthor());
        mIsbn.setText("ISBN: " + mOwnedBook.getIsbn());
        mStatus.setText(mBookInformation.getStatus().toString());
        mDescription.setText(mBookInformation.getDescription());

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
                //TODO: Add/Delete from user's favorite
                item.setIcon(R.drawable.ic_is_favorite);
                break;
            case R.id.itemMenuOwnedBookEdit:
                ToastMessage.show(this, "Editing..");
                break;
            case R.id.itemMenuOwnedBookViewRequests:
                ToastMessage.show(this, "Viewing Requests...");
                break;
            case R.id.itemMenuOwnedBookDelete:
                deleteBook();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteBook() {
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

    private void setBookPhotoView() {

        try {
            databaseHelper.getBookPictureUri(mBookInformation, new BookPhotoUrlCallBack() {
                @Override
                public void onCallback(Uri imageUri) {
                    Picasso.get()
                            .load(imageUri)
                            .placeholder(R.drawable.ic_hourglass_empty_grey_24dp)
                            .error(R.drawable.ic_book)
                            .rotate(90)
                            .into(mBookPhotoView);
                }
            });
        } catch (Exception e) {
            Log.e(ERROR_TAG_LOAD_IMAGE,e.getMessage());
        }
    }


}
