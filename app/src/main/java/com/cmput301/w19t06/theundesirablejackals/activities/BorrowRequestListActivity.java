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
    private Toolbar toolbar;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;

    public static final String TAG = "BorrowRequest";
    private MenuItem mSelectedFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_requests);
        databaseHelper = new DatabaseHelper();

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
                startActivity(new Intent(getApplicationContext(), MainHomeViewActivity.class));
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
        inflater.inflate(R.menu.menu_borrowed_requests, menu);
        MenuItem menuItem = menu.findItem(R.id.itemBorrowRequestsMenusSearch);
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
        BookRequestList toFilterThrough = requestsRecyclerViewAdapter.getDataSet();

        switch (id){
            case R.id.itemMenuBorrowedRequestDenied:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Borrowed...");
                break;
            case R.id.itemMenuBorrowedRequestAccepted:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Accepted...");
                break;
            case R.id.itemMenuBorrowedRequestRequested:
                menuTitle = item.getTitle().toString().toUpperCase();
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                requestsRecyclerViewAdapter.setDataSet(filteredItem);
                ToastMessage.show(this, "Viewing Pending...");
                break;
        }

        if(item.equals(mSelectedFilter)) {
            mSelectedFilter = null;
            item.setChecked(false);
            requestsRecyclerViewAdapter.setDataSet(requestsRecyclerViewAdapter.getDataSet());
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
                                requestsRecyclerViewAdapter.setDataSet(bookRequestList);
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

        Log.d(TAG, toSearchThrough.get(1).getBookRequested().getIsbn());
        for ( int i = 0; i< toSearchThrough.size(); i++){
            String isbn = toSearchThrough.get(i).getBookRequested().getIsbn();
            Book book = requestsRecyclerViewAdapter.getRelatedBook(isbn);
            String author = book.getAuthor();
            String title = book.getTitle();

            if(book.getTitle()!=null && ! book.getTitle().isEmpty() && title.toLowerCase().contains(userInput)){
                Log.d(TAG, title);
                listItem.addRequest(toSearchThrough.get(i));

            }
            else if(book.getAuthor()!=null && ! book.getAuthor().isEmpty()){
                Log.d(TAG, author);
            }

        }






//        for (int i = 0; i < toSearchThrough.size();i++){
//
//            String isbn = toSearchThrough.get(i).getBookRequested().getIsbn();
//            String title;
//            String author;
//
//            data.getBookFromDatabase(isbn, new BookCallback() {
//                @Override
//                public void onCallback(Book book) {
//
//                    if(book != null){
//                        if(book.getTitle()!=null && ! book.getTitle().isEmpty()){
//                            title = book.getTitle();
//                            Log.d(TAG, title);
//                        }
//                        if(book.getAuthor()!=null && ! book.getAuthor().isEmpty()){
//                            author = book.getAuthor();
//                            Log.d(TAG, author);
//                        }
//                    }
//                    Log.d()
//                }
//
//            });
//
//
////            String author = toSearchThrough.get(i).getBookRequested().getAuthor();
////            String title = toSearchThrough.get(i).getTitle();
//            if (isbn.toLowerCase().contains(userInput)) {
//
//                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));
//
//            } else if (author.toLowerCase().contains(userInput)) {
//                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));
//            } else if (title.toLowerCase().contains(userInput)) {
//                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));
//            }
//
//        }
        return true;
    }
}

