package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;

import org.w3c.dom.Text;

/**
 * Allows the user to view an owned book and do certain action that only book owners can do.
 * These  actions include editing information about the book, viewing a list of request on the book,
 * Delete the book from owned book list.
 * @author Art Limbaga
 */
public class ViewOwnedBookActivity extends AppCompatActivity {
    public final static String BOOK_FROM_RECYCLER_VIEW = "BOOK_RECYCLER_VIEW";
    private Book ownedBook;

    private TextView title;
    private TextView author;
    private TextView isbn;
    private TextView status;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);
        Intent intent = getIntent();
        ownedBook = (Book) intent.getSerializableExtra(BOOK_FROM_RECYCLER_VIEW);

        title = findViewById(R.id.textViewViewOwnedBookBookTitle);
        author = findViewById(R.id.textViewViewOwnedBookBookAuthor);
        isbn = findViewById(R.id.textViewViewOwnedBookBookISBN);
        status = findViewById(R.id.textViewViewOwnedBookBookStatus);
        description = findViewById(R.id.textViewViewOwnedBookBookDescription);

        title.setText(ownedBook.getTitle());
        author.setText(ownedBook.getAuthor());
        isbn.setText("ISBN: " + ownedBook.getIsbn());
        status.setText(ownedBook.getStatus().toString());
        description.setText(ownedBook.getDescription());
    }
}
