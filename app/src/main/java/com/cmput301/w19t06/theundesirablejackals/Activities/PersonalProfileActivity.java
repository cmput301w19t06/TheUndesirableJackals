package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.Database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.Database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.cmput301.w19t06.theundesirablejackals.User.UserInformation;
import com.google.gson.Gson;

public class PersonalProfileActivity extends AppCompatActivity {
    private Button editProfilebtn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_profile);

        databaseHelper = new DatabaseHelper(this);

        // display personal information on the activity
        displayInfo();

        editProfilebtn = (Button) findViewById(R.id.profileEditBtn_id);
    }

    public void editProfilebtnPress(View view){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }

    public void displayInfo() {
        databaseHelper.getUserInfoFromDatabase(new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInformation) {
                // retrieve user's info
                String userName = userInformation.getUserName();
                String email = userInformation.getEmail();
                String phone = userInformation.getPhoneNumber();

                // display the info
                TextView userNameView = findViewById(R.id.textView8);
                userNameView.setText(userName);

                TextView emailView = findViewById(R.id.textView12);
                emailView.setText(email);

                TextView phoneView = findViewById(R.id.textView14);
                phoneView.setText(phone);
            }
        });

    }
}

