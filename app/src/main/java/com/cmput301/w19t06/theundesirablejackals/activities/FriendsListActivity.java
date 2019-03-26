package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.adapter.FriendsRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.ArrayList;

/**
 * List of friends to the user. Allows them to add new users and view suggested users.
 * From here they can access friends profiles
 * Author: Kaya Thiessen
 */
public class FriendsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendsRecyclerViewAdapter adapter;
    private ArrayList<UserInformation> friendsList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        friendsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFriendListActivityFriendList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbarMessageActivity);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Friends");
        setSupportActionBar(toolbar);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("FriendsListActivity",((Integer)position).toString());
            }
        };

        adapter = new FriendsRecyclerViewAdapter(listener);
        recyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity.class));
                finish();
            }
        });

    }
}
