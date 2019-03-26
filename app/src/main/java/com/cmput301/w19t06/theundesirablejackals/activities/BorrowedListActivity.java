package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

/**
 * Pulls all Borrowed book requests and displays them here
 * Author: Kaya Thiessen
 */
public class BorrowedListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_requests);
        databaseHelper = new DatabaseHelper();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewBorrowRequests);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activityBorrowRequestSwipeRefreshLayout);

        toolbar = findViewById(R.id.tool_barBorrow);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Borrow Requests");
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                recyclerOnClick(view, position);
            }
        };

        requestsRecyclerViewAdapter.setMyListener(listener);
        recyclerView.setAdapter(requestsRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getBorrowRequests();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity.class));
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_borrowed_requests, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.itemMenuBorrowedRequestBorrowed:
                ToastMessage.show(this, "Viewing Borrowed...");
                break;
            case R.id.itemMenuBorrowedRequestAccepted:
                ToastMessage.show(this, "Viewing Accepted...");
                break;
            case R.id.itemMenuBorrowedRequestPending:
                ToastMessage.show(this, "Viewing Pending...");
                break;
            case R.id.itemMenuBorrowedRequestTitle:
                ToastMessage.show(this, "Title Search...");
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    public void setBorrowedRequestsAdapter(RequestsRecyclerViewAdapter adapter){
        this.requestsRecyclerViewAdapter= adapter;
    }

    private void recyclerOnClick(View view, int position){
        //TODO implement lent list click listener functionality
        BookRequestStatus status = requestsRecyclerViewAdapter.get(position).getCurrentStatus();
        Intent intent;

        if (status == BookRequestStatus.PENDING){
            ToastMessage.show(this, "Waiting On Response from Owner");
            //ToDO
            //Open up book View???
        }
        else if(status == BookRequestStatus.ACCEPTED){
            intent = new Intent(BorrowedListActivity.this, MapHandoff.class);
            startActivity(intent);
        }

        else if(status == BookRequestStatus.HANDED_OFF){
            intent = new Intent(BorrowedListActivity.this, MapHandoff.class);
            startActivity(intent);
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
}

