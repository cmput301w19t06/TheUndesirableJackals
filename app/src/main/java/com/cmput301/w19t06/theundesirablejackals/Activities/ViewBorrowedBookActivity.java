package com.cmput301.w19t06.theundesirablejackals.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Allows book borrowers to view a book they have borrowed, return the book, and view book owner's
 * profile.
 *
 * @author Art Limbaga
 */
public class ViewBorrowedBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_borrowed_book);

    }
}
