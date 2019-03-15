package com.cmput301.w19t06.theundesirablejackals.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * A general book view of a book displayed on the library tab of the app. Whoever is viewing the
 * book will be able to request the book, go to book owner profile, and view all the images
 * related to the book.
 * @author Art Limbaga
 */
public class ViewLibraryBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_library_book);
    }
}
