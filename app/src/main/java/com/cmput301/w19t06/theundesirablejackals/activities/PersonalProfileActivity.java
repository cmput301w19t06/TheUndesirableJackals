/**
 * Class launched from "MainHomeViewActivity" that displays the user's personal information
 * It gives the user the option to edit their phone number and see their current pick up
 * buttonDefaultLocation (when the appropiate buttons are pressed)
 *
 * @version 1 - March 8, 2019
 * @see MainHomeViewActivity, MapsActivity, EditContactInfoActivity
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

import java.io.File;

public class
PersonalProfileActivity extends AppCompatActivity {
    public static final int IMAGE_GALLERY_REQUEST = 700;
    private static final int GALLERY_PERMISSION_REQUEST = 701;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itemMenuPersonalProfileChangePhoneNumber):
                doPhoneNumberChange();
                break;
            case (R.id.itemMenuPersonalProfileChangeProfilePhoto):
                doProfilePhotoChange();
                break;
            default:
                return false;
        }
        return true;
    }

    private void doPhoneNumberChange() {
        LayoutInflater li = LayoutInflater.from(PersonalProfileActivity.this);
        View promptsView = li.inflate(R.layout.prompt_new_phone_number, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PersonalProfileActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextPrompNewPhoneNumberInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                mTextViewPhoneNumber.setText("Phone: " + userInput.getText());
                                mUserInformation.setPhoneNumber(userInput.getText().toString());
                                updateUserInformation();
                                ToastMessage.show(PersonalProfileActivity.this, "Phone number updated");
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
        });
    }


    public void doProfilePhotoChange() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            dispatchImageGalleryIntent();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST);
        }
    }

    private void dispatchImageGalleryIntent() {
        Intent photoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    public void updateUserInformation() {
        mDatabaseHelper.updateUserInfo(mUserInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                ToastMessage.show(PersonalProfileActivity.this, "Profile updated");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProfilePhoto.setImageResource(R.drawable.ic_loading_with_text);
        if (requestCode == PersonalProfileActivity.IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                mProfilePhoto.setImageURI(data.getData());
                mUserInformation.setUserPhoto(data.getData().getLastPathSegment());
                uploadProfileImage(data.getData());
                ToastMessage.show(PersonalProfileActivity.this, "Profile photo changed");
            } else {
                ToastMessage.show(PersonalProfileActivity.this, "Add picture was canceled");
            }
        }
    }

    private void uploadProfileImage(final Uri imageUri) {
        mDatabaseHelper.uploadProfilePicture(imageUri, mUserInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                mUserInformation.setUserPhoto(imageUri.getLastPathSegment());
                updateUserInformation();
                ToastMessage.show(PersonalProfileActivity.this, "Profile photo saved in the database");
            }
        });
    }

}

