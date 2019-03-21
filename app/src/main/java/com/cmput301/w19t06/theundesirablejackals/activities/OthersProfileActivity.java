package com.cmput301.w19t06.theundesirablejackals.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;


/**
 * Activity used to display friends profile + give option to message them
 * Author: Kaya Thiessen
 *
 * TODO: implement send message and send friend request option
 * displays the user contact info given a user name
 */

public class OthersProfileActivity extends AppCompatActivity {
    private Button sendFriendRequest;
    private Button sendMessage;
    private Button searchUser;
    private EditText userName;
    private TextView retrievedUserName;
    private TextView retrievedEmail;
    private TextView retrievedPhone;
    private DatabaseHelper databaseHelper;
//    private TextView textViewEditFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

//        buttonEditFriend = (Button) findViewById(R.id.buttonOthersProfileActivityAddFriend);
//        textViewEditFriend = (TextView) findViewById(R.id.buttonOthersProfileActivityAddFriend);

        // to be implemented
        sendFriendRequest = (Button) findViewById(R.id.buttonOthersProfileActivityAddFriend);
        sendMessage = (Button) findViewById(R.id.buttonOthersProfileActivityMessageFriend);

        // initializes search button and use info fields
        searchUser = (Button) findViewById(R.id.buttonSearch);
        retrievedUserName = (TextView) findViewById(R.id.textViewOthersProfileActivityUserName);
        retrievedEmail = (TextView) findViewById(R.id.textViewOthersProfileActivityPersonalEmail);
        retrievedPhone = (TextView) findViewById(R.id.textViewOthersProfileActivityPhoneNumber);


        // cancel action
        searchUser.setOnClickListener(new View.OnClickListener() {
            /**
             * Search user's contact info given their user name
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                // retrieve input data
                userName = (EditText) findViewById(R.id.editTextUserName);

                String user = userName.getText().toString();

                if (!user.isEmpty()) {
                    searchContactInfo(user);
                }
            }
        });


        sendFriendRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // To be implemented
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // To be implemented
            }
        });

    }

    /**
     * Initializes the database helper which will be used to retrieve contact information
     * from user specified
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseHelper = new DatabaseHelper();
    }

    /**
     * Calls method from databaseHelper to retrieve contact information from a given user name and,
     * if exists, display it on the fields
     * @param userNameSearch The user name that will be searched on Firebase
     */
    private void searchContactInfo(final String userNameSearch) {
        databaseHelper.getUserInfoFromDatabase(userNameSearch, new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInfo) {
                // if user was found display the info
                if (userInfo != null) {
                    retrievedUserName.setText(userInfo.getUserName());
                    retrievedEmail.setText(userInfo.getEmail());
                    retrievedPhone.setText(userInfo.getPhoneNumber());
                }
                else {
                    userName.setText("");
                    retrievedUserName.setText("");
                    retrievedEmail.setText("");
                    retrievedPhone.setText("");
                    Toast toast = Toast.makeText(getApplicationContext(), "Not Found",
                                  Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }


//    /**
//     * Allows user to friend or unfriend another user
//     * @param view
//     */
//    public void editFriend(View view){
//        if (buttonEditFriend.getText() == "Unfriend") {
//            buttonEditFriend.setText("Add Friend");
//        }
//        else{
//            buttonEditFriend.setText("Unfriend");
//        }
//    }
}
