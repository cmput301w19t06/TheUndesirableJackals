package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ArrayList<String> barcodes = new ArrayList<String>();
    private static final String TAG = "StartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    public void onClickSignInButton(View v) {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
