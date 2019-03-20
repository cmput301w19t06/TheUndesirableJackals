package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;

    /**
     * On create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        toolbar = findViewById(R.id.tool_barNotification);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainHomeViewActivity.class));
                finish();
            }
        });
    }

}
