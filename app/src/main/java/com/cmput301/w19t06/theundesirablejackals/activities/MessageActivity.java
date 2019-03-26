package com.cmput301.w19t06.theundesirablejackals.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.adapter.MessagesRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;


public class MessageActivity extends AppCompatActivity implements RecyclerViewClickListener,  View.OnClickListener{

    MessagesRecyclerViewAdapter messagesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

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


    @Override
    public void onClick(View view, int position) {
        ToastMessage.show(this, "Clicked on " + position);
    }

    @Override
    public void onClick(View v) {
        ToastMessage.show(this, "Clicked on Floating Action button");
    }
}
