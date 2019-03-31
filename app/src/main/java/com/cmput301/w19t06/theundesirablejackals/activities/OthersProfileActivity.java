package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.classes.FriendRequest;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserListCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;
import com.squareup.picasso.Picasso;


/**
 * Activity used to display friends profile + give option to message them
 * Author: Kaya Thiessen
 * <p>
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

        boolean alreadyFriend = intent.hasExtra(ViewFriendsListActivity.FRIENDS_ALREADY);


        mProfilePhoto = findViewById(R.id.imageViewOthersProfilePhoto);
        mTextViewUsername = findViewById(R.id.textViewOthersProfileActivityUserName);
        mTextViewEmail = findViewById(R.id.textViewOthersProfileActivityEmail);
        mTextViewPhoneNumber = findViewById(R.id.textViewOthersProfileActivityPhoneNumber);


        mMessageUser = findViewById(R.id.floatingButtonOthersProfileViewSendMessage);
        mAddUserAsFriend = findViewById(R.id.floatingButtonOtherProfileViewAddFriend);

        if(!alreadyFriend) {
            mAddUserAsFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    makeNewFriendRequest();
                }
            });
        }else{
            mAddUserAsFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("This user is already your friend!");
                }
            });
        }


        mMessageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                doSendOwnerMessageProcess();
                view.setClickable(true);

            }
        });

    }

    private void showToast(String message){
        ToastMessage.show(this, message);
    }

    public void doSendOwnerMessageProcess() {
        LayoutInflater li = LayoutInflater.from(OthersProfileActivity.this);
        View promptsView = li.inflate(R.layout.prompt_send_book_owner_message, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OthersProfileActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextPromptMessageBookOwnerInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (userInput.getText().toString().isEmpty()) {
                                    ToastMessage.show(getApplicationContext(), "Please enter something");
                                } else {
                                    sendMessage(userInput.getText().toString());
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void sendMessage(final String message) {
        mDatabaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInformation) {
                Messaging messaging = new Messaging();
                messaging.setFrom(userInformation.getUserName());
                messaging.setTo(mUserInformation.getUserName());
                messaging.setMessage(message);
                mDatabaseHelper.sendMessage(messaging, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {

                        if (bool) {
                            ToastMessage.show(getApplicationContext(), "Message sent");
                        } else {
                            ToastMessage.show(getApplicationContext(), "Message not sent");
                        }
                    }
                });
            }
        });
    }

    private void makeNewFriendRequest() {

        mDatabaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(final User user) {
                if(user != null){
                    if (mUserInformation.getUserName().equals(user.getUserInfo().getUserName())) {
                        ToastMessage.show(getApplicationContext(),
                                "How lonely do you have to be to add yourself as friend?");
                    }else {
                        mDatabaseHelper.getFriendsList(user.getUserInfo().getUserName(), new UserListCallback() {
                            @Override
                            public void onCallback(UserList userList) {
                                if (userList != null && userList.contains(mUserInformation)) {
                                    ToastMessage.show(getApplicationContext(),
                                            "Ya'll are already friends");
                                } else {
                                    sendNewFriendRequest(user.getUserInfo());
                                }
                            }
                        });
                    }
                }

            }
        });
    }

    private void sendNewFriendRequest(UserInformation sender) {
        FriendRequest newFriendRequest = new FriendRequest(sender,mUserInformation);
        mDatabaseHelper.makeFriendRequest(newFriendRequest, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                ToastMessage.show(getApplicationContext(),
                        "Friend request sent");
            }
        });
    }


    private void setProfilePhoto() {
        if (mUserInformation.getUserPhoto() != null && !mUserInformation.getUserPhoto().isEmpty()) {
            mDatabaseHelper.getProfilePictureUri(mUserInformation, new UriCallback() {
                @Override
                public void onCallback(Uri uri) {
                    if (uri != null) {
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
