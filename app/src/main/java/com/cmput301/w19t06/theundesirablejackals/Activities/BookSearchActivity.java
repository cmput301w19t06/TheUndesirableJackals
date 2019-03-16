package com.cmput301.w19t06.theundesirablejackals.activities;

/*
Code copied from https://github.com/google-developer-training/android-fundamentals/tree/master/WhoWroteIt

Modifications:
    Class name (line 46)
    Added a new text view "mPublisherText" (line 52, 70)
    Added a search button (lines 72-81)
    Changed method name (line 84)
    Sent an additional parameter for "FetchBook" constructor (line 101)
 */

/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.classes.FetchBook;

/**
 * The WhoWroteIt app query's the Book Search API for Books based
 * on a user's search.
 */
public class BookSearchActivity extends AppCompatActivity {

    // Variables for the search input field, and results TextViews.
    private EditText mBookInput;
    private EditText mTitleText;
    private EditText mAuthorText;
    private EditText mDescriptionText;
    private Button searchButton;


    /**
     * Initializes the activity.
     *
     * @param savedInstanceState The current state data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        // Initialize all the view variables.
        mBookInput = (EditText)findViewById(R.id.editTextSearchBookActivityBookTitle);
        mTitleText = (EditText)findViewById(R.id.title);
        mAuthorText = (EditText)findViewById(R.id.author);
        mDescriptionText = (EditText)findViewById(R.id.description);

        searchButton = (Button) findViewById(R.id.buttonSearchBookActivitySearchForBook);

        // search action
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                searchBooks();
            }
        });
    }


    public void searchBooks() {
        // Get the search string from the input field.
        String queryString = mBookInput.getText().toString();

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            new FetchBook(mTitleText, mAuthorText, mDescriptionText, mBookInput).execute(queryString);

        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                mAuthorText.setText("");
                mTitleText.setText("");
            } else {
                mAuthorText.setText("");
                mTitleText.setText("");
            }
        }
    }
}
