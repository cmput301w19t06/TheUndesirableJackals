package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Handles the Notification list. Shows all notifications on a clear list. Allow access to notification
 * by clicking item on list
 * Author: Kaya Thiessen
 */
public class NotificationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String[] notifications= {};

    /**
     * On create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        //TODO
        //Set the list notification to the UserNotificationList

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, notifications);
        ListView listview = (ListView) findViewById(R.id.listViewNotificationActivityNotificationList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    /**
     * On click of item list adapter. Opens corresponding issue
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
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
