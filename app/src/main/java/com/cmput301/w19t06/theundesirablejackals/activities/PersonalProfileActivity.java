/**
 * Class launched from "MainHomeViewActivity" that displays the user's personal information
 * It gives the user the option to edit their phone number and see their current pick up
 * buttonDefaultLocation (when the appropiate buttons are pressed)
 *
 * @version 1 - March 8, 2019
 * @see MainHomeViewActivity, MapsActivity, EditContactInfoActivity
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class
PersonalProfileActivity extends AppCompatActivity {


    // ratio in relation to the original display
    private final Double WIDTH_RATIO = 0.9;
    private final Double HEIGHT_RATIO = 0.6;


    private DatabaseHelper mDatabaseHelper;

    private UserInformation mUserInformation;

    private ImageView mProfilePhoto;
    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private TextView mTextViewPhoneNumber;
    private TextView mTextViewEditProfile;


    /**
     * Initializes buttons and contains button handlers that begin intents to MapsActivity
     * EditContactInfoActivity activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout((int)(width*WIDTH_RATIO), (int) (height*HEIGHT_RATIO));

        mDatabaseHelper = new DatabaseHelper();

        mProfilePhoto = findViewById(R.id.imageViewPersonalProfilePhoto);
        mTextViewUsername = findViewById(R.id.textViewPersonalProfileActivityUserName);
        mTextViewEmail = findViewById(R.id.textViewPersonalProfileActivityEmail);
        mTextViewPhoneNumber = findViewById(R.id.textViewPersonalProfileActivityPhoneNumber);
        mTextViewEditProfile = findViewById(R.id.textViewPersonalProfileActivityEditProfile);

        mTextViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalProfileActivity.this, EditPersonalProfileActivity.class);
                //intent.putExtra()
            }
        });

        getUserInfo();
    }


    /**
     * Retrieves user name, email and phone number and set those values to their respective
     * text view
     */
    public void getUserInfo() {
        mDatabaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                // retrieve user's info
                mUserInformation = user.getUserInfo();

                // display the info
                mTextViewUsername.setText(mUserInformation.getUserName());
                mTextViewEmail.setText("Email: " + mUserInformation.getEmail());
                mTextViewPhoneNumber.setText("Phone: " + mUserInformation.getPhoneNumber());
//                if (mUserInformation.getUserPhoto() != null || !mUserInformation.getUserPhoto().isEmpty()) {
//                    Picasso.get()
//                            .load(mUserInformation.getUserPhoto())
//                            .error(R.drawable.ic_person_outline_grey_24dp)
//                            .placeholder(R.drawable.ic_loading_with_text)
//                            .into(mProfilePhoto);
//                }

            }
        });
    }

//    public void testLocation() {
//        Intent i = new Intent(this, SelectLocationActivity.class);
//        startActivityForResult(i, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//                Double lat = Double.parseDouble(data.getStringExtra("lat"));
//                Double lng = Double.parseDouble(data.getStringExtra("lng"));
//
//                Toast toast = Toast.makeText(getApplicationContext(), lat+" "+lng,
//                        Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }
//        }
//    }
}

