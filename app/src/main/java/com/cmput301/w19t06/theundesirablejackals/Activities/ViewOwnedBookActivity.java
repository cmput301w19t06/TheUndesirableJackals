package com.cmput301.w19t06.theundesirablejackals.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Allows the user to view an owned book and do certain action that only book owners can do.
 * These  actions include editing information about the book, viewing a list of request on the book,
 * Delete the book from owned book list.
 * @author Art Limbaga
 */
public class ViewOwnedBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);
    }
}
