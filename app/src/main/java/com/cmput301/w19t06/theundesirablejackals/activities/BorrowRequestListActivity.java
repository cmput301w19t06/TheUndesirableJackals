package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.ArrayList;

/**
 * Pulls all Borrowed book requests and displays them here
 * Author: Kaya Thiessen
 */
public class BorrowRequestListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {

    public final static String SEARCH_BY_ISBN = "SearchByIsbnBorrow";
    private String ACTIVITY_TAG = "BorrowRequestList";

    private Toolbar toolbar;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;
    private String isbnSearch;

    public static final String TAG = "BorrowRequest";
    private MenuItem mSelectedFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_requests);
        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        if (intent != null) {
            isbnSearch = (String) intent.getSerializableExtra(SEARCH_BY_ISBN);
        }

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewBorrowRequests);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activityBorrowRequestSwipeRefreshLayout);

        ItemTouchHelper itemTouchHelper;
        SwipeController swipeController;

        toolbar = findViewById(R.id.tool_barBorrow);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Borrow Requests");
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        requestsRecyclerViewAdapter.setMyListener(this);
        recyclerView.setAdapter(requestsRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        swipeController = new SwipeController(requestsRecyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        recyclerOnClick(view, position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_requests, menu);
        MenuItem menuItem = menu.findItem(R.id.itemRequestsMenusSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String menuTitle;
        BookRequestList filteredItem = new BookRequestList();
        BookRequestList toFilterThrough = requestsRecyclerViewAdapter.getDataCopy();

        switch (id){

            case R.id.itemRequestsMenuRequested:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Requested...");
                break;
            case R.id.itemRequestsMenuDenied:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Denied...");
                break;
            case R.id.itemRequestsMenuAccepted:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Accepted...");
                break;
            case R.id.itemRequestsMenuHandedOff:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Handed off...");
                break;
            case R.id.itemRequestsMenuBorrowed:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Borrowed...");
                break;
            case R.id.itemRequestsMenuReturned:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Returned...");
                break;
        }

        if(item.equals(mSelectedFilter)) {
            mSelectedFilter = null;
            item.setChecked(false);
            requestsRecyclerViewAdapter.setDataSet(requestsRecyclerViewAdapter.getDataCopy());
        } else {
            item.setChecked(true);
            mSelectedFilter = item;
        }


        return super.onOptionsItemSelected(item);
    }

    private void recyclerOnClick(View view, int position){
        //TODO implement lent list click listener functionality
        BookRequestStatus status = requestsRecyclerViewAdapter.get(position).getCurrentStatus();
        Intent intent;

        if (status == BookRequestStatus.REQUESTED){
            doViewLibraryBook(requestsRecyclerViewAdapter.get(position).getBookRequested());
        }
        else if(status == BookRequestStatus.ACCEPTED || status == BookRequestStatus.RETURNING){
            intent = new Intent(BorrowRequestListActivity.this,
                                ViewBookRequestInfo.class);
            intent.putExtra(ViewBookRequestInfo.BOOK_REQUEST_INFO,
                    requestsRecyclerViewAdapter.get(position));
            intent.putExtra(ViewBookRequestInfo.VIEW_REQUEST_AS, ViewBookRequestInfo.ViewRequestInfoAs.BORROWER);
            startActivity(intent);

        }
        else if(status == BookRequestStatus.HANDED_OFF){
            intent = new Intent(BorrowRequestListActivity.this, ViewHandedoffBookRequestActivity.class);
            intent.putExtra(ViewHandedoffBookRequestActivity.HANDED_OFF_REQUEST, requestsRecyclerViewAdapter.get(position));
            startActivity(intent);
        }
        else if(status == BookRequestStatus.BORROWED) {
            intent = new Intent(BorrowRequestListActivity.this, ViewBorrowedBookRequestActivity.class);
            intent.putExtra(ViewBorrowedBookRequestActivity.BORROWED_REQUEST, requestsRecyclerViewAdapter.get(position));
            startActivity(intent);
        }
        else if(status == BookRequestStatus.DENIED){
            showToast("Request denied, attempting to remove request from list");
            requestsRecyclerViewAdapter.deleteItem(position);
        }

    }

    /**
     * Resets the filter menu options every new choice
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for(int i=0; i<menu.size(); i++){
            if(menu.getItem(i).equals(mSelectedFilter)) {
                continue;
            } else {
                menu.getItem(i).setChecked(false);
            }
        }
        return true;
    }

    /**
     * Filters the list of books visible on My Books tab by status
     * @param filteredItem a collection of book list with a specific status specified by a user
     * @param toFilterThrough the BookRequestList class
     * @param menuTitle status to be used when filtering the books
     */
    // filters through books by status
    private void filterThrough(BookRequestList filteredItem, BookRequestList toFilterThrough, String menuTitle) {
        for(int i = 0; i < toFilterThrough.size(); i++){
            //get the status of the book
            String status = toFilterThrough.get(i).getCurrentStatus().toString();
            Log.d(TAG, "I'm inside the filterthrough method");
            Log.d(TAG, status);
            Log.d(TAG, menuTitle);

            if(status.equals(menuTitle)){
                Log.d(TAG,"I'm inside the if statement");
                filteredItem.addRequest(toFilterThrough.get(i));

            }
        }
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getBorrowRequests();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getBorrowRequests() {
        if (currentUser == null) {
            databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
                @Override
                public void onCallback(UserInformation userInformation) {
                    if (userInformation != null) {
                        currentUser = userInformation;
                        databaseHelper.getBorrowRequests(userInformation.getUserName(), new BookRequestListCallback() {
                            @Override
                            public void onCallback(BookRequestList bookRequestList) {

                                requestsRecyclerViewAdapter.setDataCopy(bookRequestList);
                                BookRequestList searchedBookRequest = new BookRequestList();

                                if(isbnSearch!=null && !isbnSearch.isEmpty()) {
                                    for (BookRequest request: bookRequestList.getBookRequests()) {
                                        if(request.getBookRequested().getIsbn().equals(isbnSearch)) {
                                            searchedBookRequest.addRequest(request);
                                        }
                                    }
                                    isbnSearch = "";
                                    requestsRecyclerViewAdapter.setDataSet(searchedBookRequest);
                                } else {
                                    requestsRecyclerViewAdapter.setDataSet(bookRequestList);
                                }
                            }
                        });
                    }
                }
            });
        }else{
            databaseHelper.getBorrowRequests(currentUser.getUserName(), new BookRequestListCallback() {
                @Override
                public void onCallback(BookRequestList bookRequestList) {
                    requestsRecyclerViewAdapter.setDataSet(bookRequestList);
                    requestsRecyclerViewAdapter.setDataCopy(bookRequestList);

                }
            });
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showToast(String message){
        ToastMessage.show(this, message);
    }


    @Override
    protected void onResume() {
        super.onResume();

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(currentActivityReceiver);
        currentActivityReceiver = null;
        super.onPause();
    }

    @Override
    protected void onStop(){
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(currentActivityReceiver);
        currentActivityReceiver = null;
        super.onStop();
    }

    public void doViewLibraryBook(final BookInformation bookInformation) {
        databaseHelper.getBookFromDatabase(bookInformation.getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                Intent intent = new Intent(BorrowRequestListActivity.this, ViewLibraryBookActivity.class);
                intent.putExtra(ViewLibraryBookActivity.LIBRARY_BOOK_FROM_RECYCLER_VIEW, book);
                intent.putExtra(ViewLibraryBookActivity.LIBRARY_INFO_FROM_RECYCLER_VIEW, bookInformation);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        BookRequestList listItem = new BookRequestList();

        BookRequestList toSearchThrough = requestsRecyclerViewAdapter.getDataCopy();
        //Log.d(TAG, String.valueOf(toSearchThrough.size()));

        for ( int i = 0; i< toSearchThrough.size(); i++){
            String isbn = toSearchThrough.get(i).getBookRequested().getIsbn();
            Book book = requestsRecyclerViewAdapter.getRelatedBook(isbn);
            String author = book.getAuthor();
            String title = book.getTitle();

            if(book.getTitle()!=null && ! book.getTitle().isEmpty() && title.toLowerCase().contains(userInput)){
                Log.d(TAG, title);
                listItem.addRequest(toSearchThrough.get(i));
                requestsRecyclerViewAdapter.setDataSet(listItem);

            }
            else if(book.getAuthor()!=null && ! book.getAuthor().isEmpty() && author.toLowerCase().contains(userInput) ){
                Log.d(TAG, author);
                listItem.addRequest(toSearchThrough.get(i));
                requestsRecyclerViewAdapter.setDataSet(listItem);
            }
            else if(book.getIsbn()!=null && ! book.getIsbn().isEmpty() && isbn.toLowerCase().contains(userInput)){
                listItem.addRequest(toSearchThrough.get(i));
                requestsRecyclerViewAdapter.setDataSet(listItem);
            }

        }
        return true;
    }

}

