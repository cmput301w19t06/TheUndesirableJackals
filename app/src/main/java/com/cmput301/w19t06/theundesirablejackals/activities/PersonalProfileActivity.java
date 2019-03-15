/**
 * Class launched from "MainHomeViewActivity" that displays the user's personal information
 * It gives the user the option to edit their phone number and see their current pick up
 * location (when the appropiate buttons are pressed)
 * @version 1 - March 8, 2019
 * @see MainHomeViewActivity, MapsActivity, EditContactInfoActivity
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

public class PersonalProfileActivity extends AppCompatActivity {
    private Button editProfilebtn;
    private Button back;
    private Button location;
    private DatabaseHelper databaseHelper;

    /**
     * Initializes buttons and contains button handlers that begin intents to MapsActivity
     * EditContactInfoActivity activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

//        databaseHelper = new DatabaseHelper(this);
//
//        // display personal information on the activity
//        getUserInfo();

        editProfilebtn = (Button) findViewById(R.id.buttonPersonalProfileActivityEditInfo);
        back = (Button) findViewById(R.id.buttonPersonalProfileActivityBack);
        location = (Button) findViewById(R.id.buttonPersonalProfileActivityDefaultLocation);

        // edit action
        editProfilebtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the edit info option
             * Sends the user to "EditContactInfoActivity" activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditContactInfoActivity.class);
                startActivity(intent);
            }
        });

        // back action
        back.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the back option
             * Sends the user back to "MainHomeView" activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
                startActivity(intent);
            }
        });

        // location action
        location.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the my pick up location option
             * Sends the user to "MapsActivity" activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initializes the database helper which will be used to retrieve contact information
     * from current user
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseHelper = new DatabaseHelper();

        // display personal information on the activity
        getUserInfo();
    }


    /**
     * Retrieves user name, email and phone number and set those values to their respective
     * text view
     */
    public void getUserInfo () {
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                // retrieve user's info
                UserInformation userInformation = user.getUserInfo();
                String userName = userInformation.getUserName();
                String email = userInformation.getEmail();
                String phone = userInformation.getPhoneNumber();

                Geolocation b = user.getPickUpLocation();

                // display the info
                TextView userNameView = (TextView) findViewById(R.id.textViewPersonalProfileActivityUserName);
                userNameView.setText(userName);

                TextView emailView = (TextView) findViewById(R.id.textViewPersonalProfileActivityEmail);
                emailView.setText(email);

                TextView phoneView = (TextView) findViewById(R.id.textViewPersonalProfileActivityPhoneNumber);
                phoneView.setText(phone);
            }
        });
    }
}

