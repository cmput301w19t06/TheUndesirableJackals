package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.adapter.ChatRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.MessageListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private UserInformation currentUser;
    private BroadcastReceiver currentActivityReceiver;
    private boolean doneDatabaseFetch = false;
    private RecyclerView chatRecyclerView;
    private static final String TAG = "Chat activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_popup_alert);

        //WE'LL DO IT LIVE!!!!!!!!!  (https://www.youtube.com/watch?v=O_HyZ5aW76c)
        //Except this should intercept notifications from firebase an update the current chat
        currentActivityReceiver = new CurrentActivityReceiver(this);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(currentActivityReceiver, CurrentActivityReceiver.CURRENT_ACTIVITY_RECEIVER_FILTER);

        databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInformation) {
                doneDatabaseFetch = true;
                if(userInformation != null){
                    currentUser = userInformation;
                }else{
                    showToast("Error connecting to database");
                }
            }
        });
        Intent intent = getIntent();
        MessageMetaData messageMetaData = (MessageMetaData) intent.getSerializableExtra(MessageActivity.CHAT_DATA);

        Button send = findViewById(R.id.buttonChatActivityNewMessageSend);
        send.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager;


        layoutManager = new LinearLayoutManager(this);
        chatRecyclerView = findViewById(R.id.recyclerViewChatActivityMessageContent);

        chatRecyclerView.setHasFixedSize(false);

        chatRecyclerView.setLayoutManager(layoutManager);

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(messageMetaData);
        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);
        chatRecyclerView.scrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);

    }

    private void showToast(String message) {
        ToastMessage.show(this, message);
    }

    @Override
    public void onClick(View v) {
        EditText messageView = findViewById(R.id.editTextChatActivityNewMessage);
        String message = messageView.getText().toString();
        messageView.setText("");
        if(!message.isEmpty()){
            if(doneDatabaseFetch && currentUser != null){

                sendMessage(message, chatRecyclerViewAdapter.getDataSet().getUsername());
            }else if(doneDatabaseFetch){
                databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
                    @Override
                    public void onCallback(UserInformation userInformation) {
                        doneDatabaseFetch = true;
                        if(userInformation != null){
                            currentUser = userInformation;
                        }else{
                            showToast("Error connecting to database");
                        }
                    }
                });
            }else{
                showToast("It's taking a long time to talk to our servers... Sorry");
            }

        }else{
            showToast("Please type a message");
        }
    }

    private void sendMessage(String message, String toUser){
        final Messaging messaging = new Messaging();
        messaging.setFrom(currentUser.getUserName());
        messaging.setTo(toUser);
        messaging.setMessage(message);
        databaseHelper.sendMessage(messaging, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    showToast("Message sent");
                    chatRecyclerViewAdapter.addMessage(messaging);
                    chatRecyclerView.scrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);

                } else {
                    showToast("Message not sent");
                }

            }
        });
    }

    public void updateMessages(){
        getMessages();
    }


    private void getMessages() {
        if(currentUser != null){
            retrieveMessagesFromDatabase();
        }else {
            databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
                @Override
                public void onCallback(UserInformation user) {
                    if (user != null) {
                        currentUser = user;
                        retrieveMessagesFromDatabase();
                    } else {
                        Log.d(TAG, "Something went wrong getting the current user");
                    }
                }
            });
        }
    }

    private void retrieveMessagesFromDatabase(){
        databaseHelper.getMessages(currentUser.getUserName(), new MessageListCallback() {
            @Override
            public void onCallback(ArrayList<Messaging> messagingArrayList) {
                if(messagingArrayList != null){
//                    messages = messagingArrayList;
                    HashMap<String, Integer> seen = new HashMap<>();
                    ArrayList<MessageMetaData> dataSet = new ArrayList<>();
                    for(Messaging m : messagingArrayList){
                        if(seen.containsKey(m.getFrom())){
                            MessageMetaData temp = dataSet.get(seen.get(m.getFrom()));
                            temp.getMessagings().add(m);
                            if(!m.getSeen()){
                                temp.addUnseen();
                            }
                        }else if(seen.containsKey(m.getTo())) {
                            MessageMetaData temp = dataSet.get(seen.get(m.getTo()));
                            temp.getMessagings().add(m);
                            if (!m.getSeen()) {
                                temp.addUnseen();
                            }
                        }else{
                            MessageMetaData temp = new MessageMetaData();
                            if(!currentUser.getUserName().equals(m.getFrom())){
                                seen.put(m.getFrom(), dataSet.size());
                                temp.getMessagings().add(m);
                                if (!m.getSeen()) {
                                    temp.addUnseen();
                                }
                                temp.setUsername(m.getFrom());
                            }else{
                                seen.put(m.getTo(), dataSet.size());
                                temp.getMessagings().add(m);
                                if (!m.getSeen()) {
                                    temp.addUnseen();
                                }
                                temp.setUsername(m.getTo());
                            }
                            dataSet.add(temp);
                        }
                    }for(MessageMetaData m : dataSet){
                        if(m.getUsername().equals(chatRecyclerViewAdapter.getDataSet().getUsername())){
                            chatRecyclerViewAdapter.setDataSet(m);
                            chatRecyclerView.scrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                        }
                    }
                }else{
                    Log.d(TAG, "Something went wrong getting messages");
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
}

