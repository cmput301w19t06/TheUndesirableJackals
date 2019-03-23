package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;

/**
 * Allows book borrowers to view a book they have borrowed, return the book, and view book owner's
 * profile.
 *
 * @author Art Limbaga
 */
public class ViewBorrowedBookActivity extends AppCompatActivity {

    public final static String BORROWED_BOOK_FROM_RECYCLER_VIEW = "BorrowedBookFromRecyclerView";
    public final static String BORROWED_INFO_FROM_RECYCLER_VIEW = "InformationFromRecyclerView";

    private Toolbar mToolbar;

    private Book mBorrowedBook;
    private BookInformation mBookInformation;

    private Button mButtonReturnBook;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mBookOwner;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_borrowed_book);
        mToolbar =  findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Borrowed Book");
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mBorrowedBook = (Book) intent.getSerializableExtra(BORROWED_BOOK_FROM_RECYCLER_VIEW);
        mBookInformation = (BookInformation) intent.getSerializableExtra(BORROWED_INFO_FROM_RECYCLER_VIEW);

        mTitle = findViewById(R.id.textViewViewBorrowedBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewBorrowedBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewBorrowedBookBookISBN);
        mDescription = findViewById(R.id.textViewViewBorrowedBookBookDescription);
        mBookOwner = findViewById(R.id.textViewViewBorrowedBookBookOwner);
        mButtonReturnBook = findViewById(R.id.buttonBorrowedBookReturnBook);

        mTitle.setText(mBorrowedBook.getTitle());
        mAuthor.setText(mBorrowedBook.getAuthor());
        mIsbn.setText("ISBN: " + mBorrowedBook.getIsbn());
        mBookOwner.setText("Owner: " + mBookInformation.getOwner());
        mDescription.setText(mBookInformation.getDescription());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainHomeViewActivity.class));
                finish();
            }
        });

        mButtonReturnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMessage.show(getApplicationContext(), "Returning....");
            }
        });

    }
}
