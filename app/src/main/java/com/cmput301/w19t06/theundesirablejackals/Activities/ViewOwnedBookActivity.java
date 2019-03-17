package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Owned Book");
        setSupportActionBar(toolbar);

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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
}
