package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Allows use to accept of reject lend requests
 * Author: Kaya Thiessen
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends LentListActivity {

    /**
     * General creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_lend);

        Intent intent = getIntent();
        int postition = intent.getIntExtra("position",0);

        TextView username = (TextView) findViewById(R.id.UserLendRequest_Id);
        TextView title = (TextView) findViewById(R.id.titleLendRequest_id);

        //TODO
        //Get the username and book title from notification and set them

        //username.setText();
        //title.setText()

    }

    /**
     * If the accept button is pressed
     * @param view
     */
    public void accept(View view){
    //TODO
        //update status of book, and open map layout
        finish();
    }

    /**
     * If the reject button is pressed
     * @param view
     */
    public void reject(View view){
        //TODO
        //delete request
        finish();
    }
}
