package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.adapter.MessagesRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;



public class MessagesActivity extends AppCompatActivity implements RecyclerViewClickListener,  View.OnClickListener{

    private MessagesRecyclerViewAdapter messagesRecyclerViewAdapter;
    private BroadcastReceiver currentActivityReceiver;
    private DatabaseHelper databaseHelper;
    private User currentUser;

    private static final String TAG = "MessagesActivity";
    public static final String CHAT_DATA = "convo";

    private Toolbar toolbarMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbarMessages = findViewById(R.id.toolbarMessageActivity);
        toolbarMessages.setTitle("Messages");
        toolbarMessages.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbarMessages);



        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user != null){
                    currentUser = user;
                }else{
                    Log.d(TAG, "Something went wrong getting user from database");
                }
            }
        });

        ItemTouchHelper itemTouchHelper;
        SwipeController swipeController;
        SwipeRefreshLayout swipeRefreshLayout;
        RecyclerView.LayoutManager layoutManager;
        RecyclerView messagesRecyclerView;
        FloatingActionButton floatingActionButton;

        messagesRecyclerView = findViewById(R.id.recyclerViewMessageActivityPersonalMessages);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutMessageActivityPersonalMessages);
        floatingActionButton = findViewById(R.id.floatingActionButtonMessageActivity);

        floatingActionButton.setOnClickListener(this);

        messagesRecyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(layoutManager);

        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(this, swipeRefreshLayout);
        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                messagesRecyclerViewAdapter.onRefresh();
            }
        });

        swipeController = new SwipeController(messagesRecyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(messagesRecyclerView);

    }


    //This on click listener is exclusively for recycler view elements
    @Override
    public void onClick(View view, int position) {
//        showToast("Clicked on " + position);
        MessageMetaData metaData = messagesRecyclerViewAdapter.getDataSet().get(position);
        if(currentUser != null) {
            startChatActivity("Messages with " + metaData.getUsername(), metaData);
        }else{
            showToast("Something went wrong connecting to database");
        }
    }

    private void startChatActivity(String title, MessageMetaData metaData) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(CHAT_DATA, metaData);
        startActivity(intent);
    }


    //this on click listener is for Views such as buttons, edit text, etc...
    @Override
    public void onClick(View v) {
        if(currentUser != null) {
//            showToast("Clicked on Floating Action button");
            showNewMessageAlertBox("New Personal Message");

        }else{
            showToast("Something happened when talking to the database");
        }
    }

    private void showToast(String message){
        ToastMessage.show(this, message);
    }

    private void showNewMessageAlertBox(String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_message_popup_alert);
        dialog.setCanceledOnTouchOutside(true);
        TextView titleView = (TextView) dialog.findViewById(R.id.textViewNewMessageAlertTitle);
        titleView.setText(title);

        final Button send = (Button) dialog.findViewById(R.id.buttonNewMessageAlertSend);
        Button cancel = (Button) dialog.findViewById(R.id.buttonNewMessageAlertCancel);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when send button is clicked
                EditText recipientView = (EditText) dialog.findViewById(R.id.editTextNewMessageAlertUser);
                EditText messageView = (EditText) dialog.findViewById(R.id.editTextNewMessageAlertMessage);
                final String message = messageView.getText().toString();
                String recipient = recipientView.getText().toString();
                if(!message.isEmpty() && !recipient.isEmpty()) {
                    databaseHelper.getUserInfoFromDatabase(recipient, new UserInformationCallback() {
                        @Override
                        public void onCallback(UserInformation userInformation) {
                            if (userInformation != null) {
                                sendMessage(message, userInformation.getUserName());
                            } else {
                                showToast("User not found");
                            }
                        }
                    });
                }else{
                    showToast("Please fill missing fields");
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when NO button is clicked
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void sendMessage(String message, String toUser){
        Messaging messaging = new Messaging();
        messaging.setFrom(currentUser.getUserInfo().getUserName());
        messaging.setTo(toUser);
        messaging.setMessage(message);
        databaseHelper.sendMessage(messaging, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    showToast("Message sent");
                } else {
                    showToast("Message not sent");
                }
            }
        });
    }

    public void update(){
        messagesRecyclerViewAdapter.onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);
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

}
