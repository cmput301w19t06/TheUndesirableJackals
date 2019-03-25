package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.database.DataSetObservable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

public class ShowBookOwnersActivity extends AppCompatActivity {
    public static final String BOOK_OBJECT = "BookObject";
    public static final String LIST_OF_OWNERS = "ListOfOwners";

    // ratio in relation to the original display
    private final Double WIDTH_RATIO = 0.8;
    private final Double HEIGHT_RATIO = 0.85;

    private DatabaseHelper mDatabaseHelper;
    private RequestsRecyclerViewAdapter mRequestsRecyclerViewAdapter;

    private Book mClickedBook;
    private BookInformationList mClickedBookOwners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_owners);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout((int)(width*WIDTH_RATIO), (int) (height*HEIGHT_RATIO));

        // grab the data from the library list
        Intent intent = getIntent();
        mClickedBook = (Book) intent.getSerializableExtra(BOOK_OBJECT);
        mClickedBookOwners = (BookInformationList) intent.getSerializableExtra(LIST_OF_OWNERS);

        RecyclerView recyclerView;


    }
}
