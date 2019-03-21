/**
 * Activity where the user will be able to edit contact information
 * At this time gives the option to edit phone number
 * @version 1 - March 8, 2019
 * @see PersonalProfileActivity
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;


public class EditContactInfoActivity extends AppCompatActivity {
    private Button submit;
    private Button cancel;
    private DatabaseHelper databaseHelper;

    /**
     * Initializes buttons and create an instance of databaseHelper which is used to write the modified
     * data to Firebase
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            /**
             * Retrieves user's phone number and set it an edit text so user can see it
             * @param user Custom class that holds all data related to current user
             */
            @Override
            public void onCallback(User user) {
                ((EditText) findViewById(R.id.editTextEditContactInfoPhoneNumber)).setText(user.getUserInfo().getPhoneNumber());
//                updateUser(user);
            }
        });
        submit = (Button) findViewById(R.id.buttonEditContactInfoActivitySubmit);
        cancel = (Button) findViewById(R.id.buttonEditContactInfoActivityCancel);

        // submit action
        submit.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the submit option
             * Calls "getUser", updates the info and then goes back to main activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                // updates user
                getUser();
                // return to "PersonalProfileActivity
//                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
//                startActivity(intent);
            }
        });

        // cancel action
        cancel.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes back to main activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                // return to "PersonalProfileActivity
                startMainHomeView();
            }
        });
    }

    /**
     * Retrieves the User object belonging to current user and calls "updateUser"
     */
    public void getUser () {
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
//                ((EditText) findViewById(R.id.editText)).setText(user.getUserInfo().getEmail());
//                ((EditText) findViewById(R.id.editText3)).setText(user.getUserInfo().getPhoneNumber());
                updateUser(user);
            }
        });
    }

    /**
     * Retrieve input from user, checks the input is valid and proceed to write the data to Firebase
     * @param user Object containing all the information of current user
     */
    public void updateUser(User user) {
        UserInformation userInfo = user.getUserInfo();
        // retrieve new phone
        EditText phone = (EditText) findViewById(R.id.editTextEditContactInfoPhoneNumber);
        String newPhone = phone.getText().toString();

        if (!newPhone.isEmpty()) {
            userInfo.setPhoneNumber(newPhone);
        }

        // updates userInfo on branch "users" on Firebase
        databaseHelper.updateUserInfo(userInfo, new BooleanCallback() {
            /**
             * Writes the data and goes back to main activity if successful
             * @param bool Value representing the success of the operation
             */
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    startMainHomeView();
                }
            }
        });
    }

    /**
     * Called to return to main activity
     */
    public void startMainHomeView(){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
}
