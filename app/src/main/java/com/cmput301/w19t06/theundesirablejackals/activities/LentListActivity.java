package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Activity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

/**
 * List view of all current lent requests. Allows clickable options to view more on request and take action depending on status
 * Author: Kaya Thiessen
 */
public class LentListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {
    public static final int DELETE_OR_ACCEPT = 420;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    public final static String SEARCH_BY_ISBN = "SearchByIsbnLent";

    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;
    private String isbnSearch;


    public static final String TAG = "LentRequest";
    private MenuItem mSelectedFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_requets);
        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        if (intent != null) {
            isbnSearch = (String) intent.getSerializableExtra(SEARCH_BY_ISBN);
        }

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewLendRequests);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activityLendRequestSwipeRefreshLayout);

        Toolbar toolbar = findViewById(R.id.tool_barLend);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
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

        switch (id) {
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
                Log.d(TAG, menuTitle);
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

    /*
    public void setLentRequestsAdapter(RequestsRecyclerViewAdapter adapter){
        this.requestsRecyclerViewAdapter = adapter;
    }
    */
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
        Log.d(TAG, String.valueOf(toFilterThrough.size()));
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


    private void recyclerOnClick(View view, int position){
        //TODO implement lent list click listener functionality
        BookRequest clickedRequest = requestsRecyclerViewAdapter.get(position);
        BookRequestStatus bookRequestStatus = clickedRequest.getCurrentStatus();
        Intent intent;
        switch (bookRequestStatus) {
            case REQUESTED:
                intent = new Intent(LentListActivity.this, AcceptRejectLendActivity.class);
                intent.putExtra(AcceptRejectLendActivity.REQUEST_INFORMATION, requestsRecyclerViewAdapter.get(position));
                startActivity(intent);
                break;
            case DENIED:
                ToastMessage.show(LentListActivity.this, "This request will be remove once requester has seen denied request");
                break;

            case ACCEPTED:
                intent = new Intent(LentListActivity.this, ViewAcceptedLendRequestActivity.class);
                intent.putExtra(ViewAcceptedLendRequestActivity.ACCEPTED_REQUEST, requestsRecyclerViewAdapter.get(position));
                startActivity(intent);
                break;

            case HANDED_OFF:
                intent = new Intent(LentListActivity.this, ViewBookRequestInfo.class);
                intent.putExtra(ViewBookRequestInfo.BOOK_REQUEST_INFO,
                        requestsRecyclerViewAdapter.get(position));
                intent.putExtra(ViewBookRequestInfo.VIEW_REQUEST_AS, ViewBookRequestInfo.ViewRequestInfoAs.OWNER);
                startActivity(intent);
                break;

            case BORROWED:
                intent = new Intent(LentListActivity.this, ViewBookRequestInfo.class);
                intent.putExtra(ViewBookRequestInfo.BOOK_REQUEST_INFO,
                        requestsRecyclerViewAdapter.get(position));
                intent.putExtra(ViewBookRequestInfo.VIEW_REQUEST_AS, ViewBookRequestInfo.ViewRequestInfoAs.OWNER);
                startActivity(intent);
                break;


            case RETURNING:
                intent = new Intent(LentListActivity.this, ViewReturningLendRequestActivity.class);
                intent.putExtra(ViewReturningLendRequestActivity.RETURNING_REQUEST,
                        requestsRecyclerViewAdapter.get(position));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getLendRequests();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getLendRequests() {
        if (currentUser == null) {
            databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
                @Override
                public void onCallback(UserInformation userInformation) {
                    if (userInformation != null) {
                        currentUser = userInformation;
                        databaseHelper.getLendRequests(userInformation.getUserName(), new BookRequestListCallback() {
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
        } else {
            databaseHelper.getLendRequests(currentUser.getUserName(), new BookRequestListCallback() {
                @Override
                public void onCallback(BookRequestList bookRequestList) {
                    requestsRecyclerViewAdapter.setDataSet(bookRequestList);
                    requestsRecyclerViewAdapter.setDataCopy(bookRequestList);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DELETE_OR_ACCEPT:
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra("resultAD");
                    if (result == "true") {
                        //for all elements in Recycler view. If book title = same title, delete
                        ToastMessage.show(getApplicationContext(), "Request Accepted");
                    } else if (result == "false") {
                        //databaseHelper.deleteRequest()
                        ToastMessage.show(getApplicationContext(), "Request Deleted");
                        //    }
                        // });

                    }
                }
        }
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
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(currentActivityReceiver);
        currentActivityReceiver = null;
        super.onStop();
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
        Log.d(TAG, String.valueOf(toSearchThrough.size()));

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
