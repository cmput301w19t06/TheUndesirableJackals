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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
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
 * List view of all current lent requests. Allow the user to view more about them
 * Author: Kaya Thiessen
 */
public class LentListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {
    public static final int DELETE_OR_ACCEPT = 420;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_requets);
        databaseHelper = new DatabaseHelper();

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
        inflater.inflate(R.menu.menu_requests, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.itemMenuLentLent:
                ToastMessage.show(this, "Viewing Currently Lent...");
                break;
            case R.id.itemMenuLentAccepted:
                ToastMessage.show(this, "Viewing Accepted...");
                break;
            case R.id.itemMenuLentRequested:
                ToastMessage.show(this, "Viewing Requested...");
                break;
            case R.id.itemMenuLentTitle:
                ToastMessage.show(this, "Title Search...");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void setLentRequestsAdapter(RequestsRecyclerViewAdapter adapter){
        this.requestsRecyclerViewAdapter = adapter;
    }
    */

    private void recyclerOnClick(View view, int position) {
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
                                requestsRecyclerViewAdapter.setDataSet(bookRequestList);
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
}
