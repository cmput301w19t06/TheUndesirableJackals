package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;


/**
 * Activity used to display friends profile + give option to message them
 * Author: Kaya Thiessen
 *
 * TODO: implement send message and send friend request option
 * displays the user contact info given a user name
 */

public class OthersProfileActivity extends AppCompatActivity {
    // ratio in relation to the original display
    private final Double WIDTH_RATIO = 0.9;
    private final Double HEIGHT_RATIO = 0.6;

    public static final String USERNAME = "OtherUserInformation";


    private DatabaseHelper mDatabaseHelper;
    private UserInformation mUserInformation;

    private ImageView mProfilePhoto;
    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private TextView mTextViewPhoneNumber;

    private FloatingActionButton mMessageUser;
    private FloatingActionButton mAddUserAsFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set size of the popup window
        getWindow().setLayout((int) (width * WIDTH_RATIO), (int) (height * HEIGHT_RATIO));

        mDatabaseHelper = new DatabaseHelper();

        Intent intent = getIntent();

        mDatabaseHelper.getUserInfoFromDatabase((String) intent.getSerializableExtra(USERNAME), new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInformation) {
                mUserInformation = userInformation;
                mTextViewUsername.setText(mUserInformation.getUserName());
                mTextViewEmail.setText(mUserInformation.getEmail());
                mTextViewPhoneNumber.setText("Phone: " + mUserInformation.getPhoneNumber());

                setProfilePhoto();
            }
        });

        mProfilePhoto = findViewById(R.id.imageViewOthersProfilePhoto);
        mTextViewUsername = findViewById(R.id.textViewOthersProfileActivityUserName);
        mTextViewEmail = findViewById(R.id.textViewOthersProfileActivityEmail);
        mTextViewPhoneNumber = findViewById(R.id.textViewOthersProfileActivityPhoneNumber);


        mMessageUser = findViewById(R.id.floatingButtonOthersProfileViewSendMessage);
        mAddUserAsFriend = findViewById(R.id.floatingButtonOtherProfileViewAddFriend);

        mAddUserAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMessage.show(OthersProfileActivity.this, "Adding user as friend..");
            }
        });


        mMessageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMessage.show(OthersProfileActivity.this, "Messaging...");
            }
        });

    }

    private void setProfilePhoto() {
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
}
