package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotification;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotificationList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String[] notifications= {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_view);

        //TODO
        //Set the list notification to the UserNotificationList

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, notifications);
        ListView listview = (ListView) findViewById(R.id.notfication_List);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        //TODO
        //Depending on content open corresponding section (message, lent, borrowed ect.)

        //intent.putExtra("position", position)
        //intent.putExtra("id",id)
        //startActivity(intent)
    }
}
