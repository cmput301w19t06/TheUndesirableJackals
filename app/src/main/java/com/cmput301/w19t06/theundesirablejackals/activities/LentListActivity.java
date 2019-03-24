package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Activity;
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

import com.cmput301.w19t06.theundesirablejackals.activities.AcceptRejectLendActivity;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RequestsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

/**
 * List view of all current lent requests. Allow the user to view more about them
 * Author: Kaya Thiessen
 */
public class LentListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    public static final int DELETE_OR_ACCEPT = 420;
    public static final int LENT_OR_ACCEPTED = 430;
    private Toolbar toolbar;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_requets);
        databaseHelper = new DatabaseHelper();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewLendRequests);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activityLendRequestSwipeRefreshLayout);

        toolbar = findViewById(R.id.tool_barLend);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
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
                getLendRequests();
                swipeRefreshLayout.setRefreshing(false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lend_request, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
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

    public void setLentRequestsAdapter(RequestsRecyclerViewAdapter adapter){
        this.requestsRecyclerViewAdapter = adapter;
    }

    private void recyclerOnClick(View view, int position){
        //TODO implement lent list click listener functionality

        //if status = Pending
        Intent intent = new Intent(LentListActivity.this, AcceptRejectLendActivity.class);
        //put in the intent!!
        startActivityForResult(intent, DELETE_OR_ACCEPT);

        //if status = Accepted or Lent
        //Open up previously set map
        //option to scan ISBN
        //on return update book status
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
        }else{
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
        switch(requestCode){
            case DELETE_OR_ACCEPT:
                if (resultCode == Activity.RESULT_OK){
                    String result = data.getStringExtra("resultAD");
                    if (result == "true"){
                        //for all elements in Recycler view. If book title = same title, delete
                        ToastMessage.show(getApplicationContext(), "Request Accepted");
                    }
                    else if (result == "false"){
                        //databaseHelper.deleteRequest()
                                ToastMessage.show(getApplicationContext(), "Request Deleted");
                        //    }
                       // });

                    }
                }
            case LENT_OR_ACCEPTED:
                if (resultCode == Activity.RESULT_OK){
                    String result2 = data.getStringExtra("lentAccept");
                    if (result2 == "lent"){
                        //delete request, update status of book to available
                    }
                    else if (result2 == "accepted"){
                        //update status to lent
                    }
                }
        }
    }


}
