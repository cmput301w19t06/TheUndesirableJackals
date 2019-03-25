package com.cmput301.w19t06.theundesirablejackals.classes;

/*
Code copied from https://github.com/google-developer-training/android-fundamentals/tree/master/WhoWroteIt

Modifications:
    Added a new attribute "mPublisherText" as a TextView (line 53)
    Added a new attributes "mCategoryText" and "mThumbnailText" as a TextView
    Added additional four parameters for "FetchBook" constructor (line 59, 62)
    Added Strings publisher, categories and thumbnail
    Set string to variables publisher, categories and thumbnail
    Set string to TextView "mThumbnailText"
    Set string to TextView "mPublisherText"
    Set string to TextView "mCategoryText"

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

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class FetchBook extends AsyncTask<String,Void,String>{

//    // Variables for the search input field, and results TextViews
    private EditText mBookInput;
    private EditText mTitleText;
    private EditText mAuthorText;
    private EditText mDescriptionText;
    private EditText mCategoriesText;
    private EditText mThumbnailText;

    // Class name for Log tag
    private static final String LOG_TAG = FetchBook.class.getSimpleName();

    // Constructor providing a reference to the views in MainActivity
    public FetchBook(EditText titleText, EditText authorText, EditText descriptionText, EditText bookInput,
                     EditText categoriesInput, EditText thumbnailInput) {
        this.mTitleText = titleText;
        this.mAuthorText = authorText;
        this.mDescriptionText = descriptionText;
        this.mBookInput = bookInput;
        this.mCategoriesText = categoriesInput;
        this.mThumbnailText = thumbnailInput;
    }

    public FetchBook() {

    }


    /**
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */
    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?q=isbn:";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // set the query URL modified
            String searchURL = BOOK_BASE_URL + queryString;
            URL requestURL = new URL(searchURL);

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;
            String description = null;
            String categories = null;
            String thumbnail = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    //title = volumeInfo.getString("title");
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                    description = volumeInfo.getString("description");
                    categories = volumeInfo.getString("categories");
                    thumbnail = imageLinks.getString("thumbnail");
                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If found, display the result.
            if (title != null) {
                mTitleText.setText(title);
            } else {
                mTitleText.setText("");
            }

            if (authors != null) {
                // fixing author string gotten from google API
                String author = authors.replace("[\"", "");
                author = author.replace("\"]", "");
                author = author.replace("\"", "");
                mAuthorText.setText(author);
            } else {
                mAuthorText.setText("");
            }

            if (description != null) {
                mDescriptionText.setText(description);
            } else {
                mDescriptionText.setText("");
            }

            if (categories != null) {
                // fixing categories string gotten from google API
                String cat = categories.replace("[\"", "");
                cat = cat.replace("\"]", "");
                cat = cat.replace("\"", "");
                mCategoriesText.setText(cat);
            } else{
                mCategoriesText.setText("");
            }

            if (thumbnail != null) {
                mThumbnailText.setText(thumbnail);
            } else {
                mThumbnailText.setText("");
            }

//            if (title != null && authors != null && description != null && categories != null){
//                mTitleText.setText(title);
//
//                // fixing author string gotten from google API
//                String author = authors.replace("[\"", "");
//                author = author.replace("\"]", "");
//                author = author.replace("\"", "");
//
//                mAuthorText.setText(author);
//
//                mDescriptionText.setText(description);
//
//                // fixing categories string gotten from google API
//                String cat = categories.replace("[\"", "");
//                cat = cat.replace("\"]", "");
//                cat = cat.replace("\"", "");
//
//                mCategoriesText.setText(cat);
//
//                //mBookInput.setText("");
//            } else {
//                // If none are found, update the UI to show failed results.
//                mTitleText.setText("");
//
//                mAuthorText.setText("");
//
//                mDescriptionText.setText("");
//            }

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
//            mTitleText.setText("");
//
//            mAuthorText.setText("");
//
//            mDescriptionText.setText("");
            e.printStackTrace();
        }
    }
}