package com.cmput301.w19t06.theundesirablejackals.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Pulls all Borrowed book requests and displays them here
 * Author: Kaya Thiessen
 */
public class BorrowedListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_requests);

        String[] borrowOptions = {};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, borrowOptions);
        ListView listview = (ListView) findViewById(R.id.borrowRequest_list);
        listview.setAdapter(adapter);
        listview.setOnClickListener((View.OnClickListener) this);
    }

    /**
     * on item clicked in list, do appropriate actions
     * @param l
     * @param v
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //TODO
        //Show where pick up if accepted
        //allow user to delete request --> bring to book and allow to unrequest by hitting request button
    }
}
