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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class
PersonalProfileActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;

    private UserInformation mUserInformation;

    private Toolbar mToolBar;

    private ImageView mProfilePhoto;
    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private TextView mTextViewPhoneNumber;
    private TextView mTextViewEditProfile;

    private RecyclerView mRecyclerViewFavouriteBooks;
    private BooksRecyclerViewAdapter mAdapterFavouriteBooks;


    /**
     * Initializes buttons and contains button handlers that begin intents to MapsActivity
     * EditContactInfoActivity activities
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setNavigationIcon(R.drawable.ic_action_back);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);

        mDatabaseHelper = new DatabaseHelper();

        mProfilePhoto = findViewById(R.id.imageViewPersonalProfilePhoto);
        mTextViewUsername = findViewById(R.id.textViewPersonalProfileActivityUserName);
        mTextViewEmail = findViewById(R.id.textViewPersonalProfileActivityEmail);
        mTextViewPhoneNumber = findViewById(R.id.textViewPersonalProfileActivityPhoneNumber);

        mRecyclerViewFavouriteBooks = findViewById(R.id.recylerViewPersonalProfileFavouriteBooks);

        getUserInfo();

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_personal_profile, menu);
        return true;
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
                mTextViewEmail.setText("email: " + mUserInformation.getEmail());
                mTextViewPhoneNumber.setText("phone: " + mUserInformation.getPhoneNumber());

                if (mUserInformation.getUserPhoto() != null && !mUserInformation.getUserPhoto().isEmpty()) {
                    mDatabaseHelper.getProfilePictureUri(mUserInformation, new UriCallback() {
                        @Override
                        public void onCallback(Uri uri) {
                            if (uri !=null) {
                                Picasso.get()
                                        .load(uri)
                                        .error(R.drawable.ic_person_outline_grey_24dp)
                                        .placeholder(R.drawable.ic_loading_with_text)
                                        .into(mProfilePhoto);
                            }
                        }

                    });
                }
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

