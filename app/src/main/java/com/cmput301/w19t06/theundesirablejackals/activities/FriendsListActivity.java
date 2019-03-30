package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.FriendsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.ArrayList;

/**
 * List of friends to the user. Allows them to add new users and view suggested users.
 * From here they can access friends profiles
 * Author: Kaya Thiessen
 */
public class FriendsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {
    private FriendsRecyclerViewAdapter FriendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter();
    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        databaseHelper = new DatabaseHelper();

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFriendListActivityFriendList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activitySwipeFriendsListRefreshLayout);

        ItemTouchHelper itemTouchHelper;
        SwipeController swipeController;

        toolbar = findViewById(R.id.tool_barFriendsList);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Friends");
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FriendsRecyclerViewAdapter.setMyListener((RecyclerViewClickListener) this);
        recyclerView.setAdapter(FriendsRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        swipeController = new SwipeController(FriendsRecyclerViewAdapter);
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

    private void recyclerOnClick(View view, int position) {
        //TODO implement lent list click listener functionality
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getFriendsList();
        swipeRefreshLayout.setRefreshing(false);
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
    private void getFriendsList() {
        if (currentUser == null) {
            databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
                @Override
                public void onCallback(UserInformation userInformation) {
                    if (userInformation != null) {
                        currentUser = userInformation;
//                        databaseHelper.getReceivedFriendRequests(userInformation.getUserName(), new BookRequestListCallback() {
//                            @Override
//                            public void onCallback(BookRequestList bookRequestList) {
//                                FriendsRecyclerViewAdapter.setDataSet(bookRequestList);
//                            }
//                        });
                    }
                }
            });
        }else{
            databaseHelper.getBorrowRequests(currentUser.getUserName(), new BookRequestListCallback() {
                @Override
                public void onCallback(BookRequestList bookRequestList) {
                    //FriendsRecyclerViewAdapter.setDataSet(bookRequestList);
                }
            });
        }
    }
}

