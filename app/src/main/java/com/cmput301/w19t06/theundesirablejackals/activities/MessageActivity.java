package com.cmput301.w19t06.theundesirablejackals.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.MessagesRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

import java.util.Random;


public class MessageActivity extends AppCompatActivity implements RecyclerViewClickListener,  View.OnClickListener{

    MessagesRecyclerViewAdapter messagesRecyclerViewAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        databaseHelper = new DatabaseHelper();

//        ItemTouchHelper itemTouchHelper;
//        SwipeController swipeController;
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

//        swipeController = new SwipeController(messagesRecyclerViewAdapter);
//        itemTouchHelper = new ItemTouchHelper(swipeController);
//        itemTouchHelper.attachToRecyclerView(messagesRecyclerView);

    }


    //This on click listener is exclusively for recycler view elements
    @Override
    public void onClick(View view, int position) {
        showToast("Clicked on " + position);
    }


    //this on click listener is for Views such as buttons, edit text, etc...
    @Override
    public void onClick(View v) {
        showToast("Clicked on Floating Action button");
        Messaging messaging = new Messaging();
        messaging.setFrom("omae_wa_mou_shindeiru");
        messaging.setTo("ultilink3");
        Random random = new Random();
        Integer integer = random.nextInt();
        messaging.setMessage("This is a test message " + integer);
        databaseHelper.sendMessage(messaging, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    showToast("Test message sent");
                }else{
                    showToast("Test message not sent");
                }

            }
        });
    }

    public void showToast(String message){
        ToastMessage.show(this, message);
    }
}
