package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;

/**
 * Allows the user to view an owned book and do certain action that only book owners can do.
 * These  actions include editing information about the book, viewing a list of request on the book,
 * Delete the book from owned book list.
 * @author Art Limbaga
 */
public class ViewOwnedBookActivity extends AppCompatActivity {

    public final static String OWNED_BOOK_FROM_RECYCLER_VIEW = "OwnedBookFromRecyclerView";

    private Toolbar mToolbar;

    private Book mOwnedBook;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mStatus;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);
        mToolbar =  findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Owned Book");
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mOwnedBook = (Book) intent.getSerializableExtra(OWNED_BOOK_FROM_RECYCLER_VIEW);

        mTitle = findViewById(R.id.textViewViewOwnedBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewOwnedBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewOwnedBookBookISBN);
        mStatus = findViewById(R.id.textViewViewOwnedBookBookStatus);
        mDescription = findViewById(R.id.textViewViewOwnedBookBookDescription);

        mTitle.setText(mOwnedBook.getTitle());
        mAuthor.setText(mOwnedBook.getAuthor());
        mIsbn.setText("ISBN: " + mOwnedBook.getIsbn());
        mStatus.setText(mOwnedBook.getStatus().toString());
        mDescription.setText(mOwnedBook.getDescription());


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainHomeViewActivity.class));
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

        switch (id){
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
                ToastMessage.show(this, "Deleting...");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
