package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

/**
 * Allows use to accept of reject lend requests
 * Author: Kaya Thiessen
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity {
    private Toolbar toolbar;
    private DatabaseHelper databaseHelper;
    /**
     * General creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_lend);

        toolbar = findViewById(R.id.tool_barAcceptReject);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper();

        Intent intent = getIntent();
        //pull the intent

        TextView username = (TextView) findViewById(R.id.textViewAcceptRejectActivityUserRequesting);
        TextView title = (TextView) findViewById(R.id.textViewAcceptRejectActivityBookTitle);

        //Set the values
        //username.setText();
        //title.setText();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity.class));
                finish();
            }
        });

    }

    /**
     * If the accept button is pressed
     * @param view
     */
    public void accept(View view){
    //TODO
        Intent intent = new Intent(AcceptRejectLendActivity.this, MapsActivity.class);
        startActivity(intent);
        //Return True, If True delete all other requests regarding this book
        finish();
    }

    /**
     * If the reject button is pressed
     * @param view
     */
    public void reject(View view){
        //TODO
        //Return False, If False delete only this request
        finish();
    }
}
