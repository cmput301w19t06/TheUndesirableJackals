package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.adapter.ChatRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private UserInformation currentUser;
    private boolean doneDatabaseFetch = false;
    private RecyclerView chatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_popup_alert);
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
}
