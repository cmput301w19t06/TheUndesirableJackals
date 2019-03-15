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
    private Button buttonEditFriend;
    private TextView textViewEditFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        buttonEditFriend = (Button) findViewById(R.id.buttonOthersProfileActivityAddFriend);
        textViewEditFriend = (TextView) findViewById(R.id.buttonOthersProfileActivityAddFriend);
    }

    /**
     * Allows user to friend or unfriend another user
     * @param view
     */
    public void editFriend(View view){
        if (buttonEditFriend.getText() == "Unfriend") {
            buttonEditFriend.setText("Add Friend");
        }
        else{
            buttonEditFriend.setText("Unfriend");
        }
    }
}
