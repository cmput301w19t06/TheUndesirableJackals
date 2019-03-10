package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_profile);

//        databaseHelper = new DatabaseHelper(this);
//
//        // display personal information on the activity
//        getUserInfo();

        editProfilebtn = (Button) findViewById(R.id.profileEditBtn_id);
        back = (Button) findViewById(R.id.Back);
        location = (Button) findViewById(R.id.location);

        // edit action
        editProfilebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditContactInfo.class);
                startActivity(intent);
            }
        });

        // back action
        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
                startActivity(intent);
            }
        });

        // location action
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseHelper = new DatabaseHelper();

        // display personal information on the activity
        getUserInfo();
    }


    public void getUserInfo () {
        databaseHelper.getUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                // retrieve user's info
                UserInformation userInformation = user.getUserInfo();
                String userName = userInformation.getUserName();
                String email = userInformation.getEmail();
                String phone = userInformation.getPhoneNumber();

                Geolocation b = user.getPickUpLocation();

                // display the info
//                TextView userNameView = findViewById(R.id.textView8);
//                userNameView.setText(userName);
//
//                TextView emailView = findViewById(R.id.textView12);
//                emailView.setText(email);
//
//                TextView phoneView = findViewById(R.id.textView14);
//                phoneView.setText(phone);
            }
        });
    }
}

