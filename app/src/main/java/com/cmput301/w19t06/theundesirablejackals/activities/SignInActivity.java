package com.cmput301.w19t06.theundesirablejackals.activities;

//import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
//import com.firebase.ui.auth.IdpResponse;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    public static final int REQUEST_SIGN_IN = 100;
    private static final int SIGN_IN_AND_AUTH = 101;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_loading);
        createSignInIntent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_AND_AUTH) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //Please get the user's phone number and password now
//                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "Sign in complete and good, checking registration");
                databaseHelper = new DatabaseHelper();
                Log.d(TAG, "Created Database helper");
                databaseHelper.isRegistered(new BooleanCallback() {
                                @Override
                                public void onCallback(boolean bool) {
                                    if(bool){
                                        Log.d(TAG, "User is registered already, finishing up now...");
                                        startMainHomeActivity();
                                    }else{
                                        Log.d(TAG, "User is not registered, start registration");
                                        registerUser();
                                    }
                                }
                            });

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...

                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response != null){
                    Log.e(TAG, String.valueOf(response));
                }
                finish();
            }
        }
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(true)
                        .setAvailableProviders(providers)
                        .build(),
                SIGN_IN_AND_AUTH);
        // [END auth_fui_create_intent]
    }

    public void registerUser(){
        setContentView(R.layout.activity_sign_in);
        findViewById(R.id.buttonCheckAvailable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((TextView) findViewById(R.id.editSignInTextUserName)).getText().toString();
                final String phone = ((TextView) findViewById(R.id.editTextSignInPhoneNumber)).getText().toString();
                validateFields(username, phone);
            }
        });
    }

    private void validateFields(final String username,final String phone){
        if(!username.isEmpty() && !phone.isEmpty()) {
            databaseHelper.isUsernameAvailable(
                    username,
                    new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if (bool) {
                                registerInDatabase(username, phone);
                            } else {
                                showMyToast("Name/Phone number not available");
                            }
                        }
                    }
            );
        }
    }

    private void registerInDatabase(final String username, final String phone){

        User user = new User(username, databaseHelper.getCurrentUser().getEmail(), phone);
        databaseHelper.registerUser(user, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if (bool) {
                        showMyToast("SUCCESS! you are registered");
                        startMainHomeActivity();
                    } else {
                        showMyToast("Failure, database connection error");
                    }
                }
            });
    }

    private void startMainHomeActivity() {
        Intent intent = new Intent(SignInActivity.this, MainHomeViewActivity.class);
        startActivity(intent);
        finish();
    }


    private void showMyToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);;
        toast.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        databaseHelper.signOut();

    }
}

