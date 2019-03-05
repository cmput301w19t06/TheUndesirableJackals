package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cmput301.w19t06.theundesirablejackals.Adapter.FriendsListAdapter;
import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.User.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FriendsListAdapter adapter;
    List<UserInformation> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list_view);

        friendsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.Friendslist_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}