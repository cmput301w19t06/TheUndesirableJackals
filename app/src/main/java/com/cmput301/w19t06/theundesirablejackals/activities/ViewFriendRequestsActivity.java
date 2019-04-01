package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.FriendRequestRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.InternalClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.FriendRequest;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.classes.FriendRequest;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.FriendRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserListCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;

import java.util.ArrayList;

import static com.cmput301.w19t06.theundesirablejackals.activities.ViewFriendsListActivity.FRIENDS_ALREADY;

/**
 * Allows user to view and accept/decline friend requests
 *
 * @author Kaya Thiessen
 * @author Devon Deweert
 */

public class ViewFriendRequestsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener, InternalClickListener {
    private FriendRequestRecyclerViewAdapter friendRequestRecyclerViewAdapter = new FriendRequestRecyclerViewAdapter();
    private BroadcastReceiver currentActivityReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private UserInformation currentUser;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_requests);
        databaseHelper = new DatabaseHelper();

        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFriendRequestListActivityFriends);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activitySwipeFriendRequestListRefreshLayout);

        ItemTouchHelper itemTouchHelper;
        SwipeController swipeController;

        toolbar = findViewById(R.id.tool_barFriendRequestList);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Friend Requests");
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        friendRequestRecyclerViewAdapter.setMyListener(this);
        friendRequestRecyclerViewAdapter.setInternalClickListener(this);
        recyclerView.setAdapter(friendRequestRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        swipeController = new SwipeController(friendRequestRecyclerViewAdapter);
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

    private void recyclerOnClick(View view, int position) {
        //TODO implement lent list click listener functionality
        //TODO Bring up the ViewOther'sProfile Activity
        FriendRequest friendRequest = friendRequestRecyclerViewAdapter.getRequest(position);
        final UserInformation userInformation = friendRequest.getRequestSender();

        Intent intent = new Intent(ViewFriendRequestsActivity.this, com.cmput301.w19t06.theundesirablejackals.activities.OthersProfileActivity.class);
        intent.putExtra(OthersProfileActivity.USERNAME, userInformation.getUserName());
        intent.putExtra(FRIENDS_ALREADY, false);
        startActivity(intent);

        view.setClickable(true);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getFriendRequestList();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getFriendRequestList() {
        friendRequestRecyclerViewAdapter.setDataSet(new ArrayList<FriendRequest>());
        databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation user) {
                if(user != null){
                    databaseHelper.getReceivedFriendRequests(user, new FriendRequestListCallback() {
                        @Override
                        public void onCallback(ArrayList<FriendRequest> friendRequests) {
                            if(friendRequests != null){
                                friendRequestRecyclerViewAdapter.setDataSet(friendRequests);
                            }
                        }
                    });
                }
            }
        });
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



    private void showToast(String message){
        ToastMessage.show(this, message);
    }

    @Override
    public void onAcceptClick(int position) {
        FriendRequest friendRequest = friendRequestRecyclerViewAdapter.getRequest(position);
        final UserInformation currentUser = friendRequest.getRequestReceiver();
        final UserInformation sender = friendRequest.getRequestSender();
        friendRequestRecyclerViewAdapter.deleteItem(position);
        databaseHelper.deleteFriendRequest(friendRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    databaseHelper.getFriendsList(currentUser.getUserName(), new UserListCallback() {
                        @Override
                        public void onCallback(UserList userList) {
                            if (userList != null) {
                                if(userList.contains(sender)) {
                                    showToast("Friend already exists");
                                    return;
                                }else {
                                    userList.getUserlist().add(0, sender);
                                    databaseHelper.updateFriendsList(currentUser.getUserName(), userList, new BooleanCallback() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            if (bool) {
                                                showToast("Friend added");
                                            } else {
                                                showToast("Something went wrong");
                                            }
                                        }
                                    });
                                }
                            } else {
                                showToast("Something went wrong");
                            }
                        }
                    });
                    databaseHelper.getFriendsList(sender.getUserName(), new UserListCallback() {
                        @Override
                        public void onCallback(UserList userList) {
                            if (userList != null) {
                                if(userList.contains(currentUser)) {
                                    showToast("Friend already exists");
                                    return;
                                }else {
                                    userList.getUserlist().add(0, currentUser);
                                    databaseHelper.updateFriendsList(sender.getUserName(), userList, new BooleanCallback() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            if (bool) {
                                                showToast("Friend added");
                                            } else {
                                                showToast("Something went wrong");
                                            }
                                        }
                                    });
                                }
                            } else {
                                showToast("Something went wrong");
                            }
                        }
                    });
                }else{
                    showToast("Something went wrong");
                }
            }
        });
    }

    @Override
    public void onDeclineClick(final int position) {
        FriendRequest friendRequest = friendRequestRecyclerViewAdapter.getRequest(position);
        databaseHelper.deleteFriendRequest(friendRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                friendRequestRecyclerViewAdapter.deleteItem(position);
                if(bool) {
                    showToast("DeclineClick");
                }else{
                    showToast("Something went wrong");
                }
            }
        });
    }
}
