package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.database.DataSetObservable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

public class ShowBookOwnersActivity extends AppCompatActivity{
    public static final String BOOK_OBJECT = "BookObject";
    public static final String LIST_OF_OWNERS = "ListOfOwners";

    // ratio in relation to the original display
    private final Double WIDTH_RATIO = 0.9;
    private final Double HEIGHT_RATIO = 0.85;

    private DatabaseHelper mDatabaseHelper;
    private RecyclerView mBookOwnersRecyclerView;
    private BooksRecyclerViewAdapter mBookOwnersRecyclerViewAdapter;

    private Book mClickedBook;
    private BookInformationList mClickedBookOwners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_owners);

        mDatabaseHelper = new DatabaseHelper();

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

        mBookOwnersRecyclerView = findViewById(R.id.recyclerViewShowBookOwners);
        mBookOwnersRecyclerView.setHasFixedSize(true);
        mBookOwnersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        RecyclerViewClickListener clickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookInformation bookInfo = mClickedBookOwners.get(position);
                Intent intent = new Intent(ShowBookOwnersActivity.this, ViewLibraryBookActivity.class);
                intent.putExtra(ViewLibraryBookActivity.LIBRARY_BOOK_FROM_RECYCLER_VIEW, mClickedBook);
                intent.putExtra(ViewLibraryBookActivity.LIBRARY_INFO_FROM_RECYCLER_VIEW, bookInfo);
                startActivity(intent);
            }
        };

        mBookOwnersRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        mBookOwnersRecyclerViewAdapter.setMyListener(clickListener);
        mBookOwnersRecyclerView.setAdapter(mBookOwnersRecyclerViewAdapter);
        BookInformationPairing bookInformationPairing = new BookInformationPairing();
        for(BookInformation bookInfo:mClickedBookOwners.getBookInformations()) {
            bookInformationPairing.addSingle(mClickedBook);
        }
        mBookOwnersRecyclerViewAdapter.setDataSet(bookInformationPairing);



    }

}
