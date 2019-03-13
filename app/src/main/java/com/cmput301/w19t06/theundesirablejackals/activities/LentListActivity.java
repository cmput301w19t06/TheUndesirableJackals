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
 * List view of all current lent requests. Allow the user to view more about them
 * Author: Kaya Thiessen
 */
public class LentListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_requets);
        String[] lendOptions = {};
        //TODO
        //Pull values that are lend requests and add to lendOptions

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lendOptions);
        ListView listview = (ListView) findViewById(R.id.listViewLendrequests);
        listview.setAdapter(adapter);
        listview.setOnClickListener((View.OnClickListener) this);
    }

    /**
     * Opens AcceptRejectLendActivity when pressed with data from list
     * @param l
     * @param v
     * @param position
     * @param id
     * @see AcceptRejectLendActivity
     */
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, AcceptRejectLendActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}