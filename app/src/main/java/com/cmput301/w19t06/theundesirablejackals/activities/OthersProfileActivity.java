package com.cmput301.w19t06.theundesirablejackals.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity used to display friends profile + give option to message them
 * Author: Kaya Thiessen
 */

public class OthersProfileActivity extends AppCompatActivity {
    private Button editFriendbtn;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        editFriendbtn = (Button) findViewById(R.id.editFriend_id);
        txt = (TextView) findViewById(R.id.editFriend_id);
    }

    /**
     * Allows user to friend or unfriend another user
     * @param view
     */
    public void editFriend(View view){
        if (editFriendbtn.getText() == "Unfriend") {
            editFriendbtn.setText("Add Friend");
        }
        else{
            editFriendbtn.setText("Unfriend");
        }
    }
}
