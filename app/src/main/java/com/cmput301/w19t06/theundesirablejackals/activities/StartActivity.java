package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

        final DatabaseHelper databaseHelper = new DatabaseHelper();


        // check if a user is already logged in
        if (databaseHelper.isUserLoggedin()) {
            // if a user is logged, continue to MainHomeActivity
            FirebaseInstanceId
                    .getInstance()
                    .getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if(task.isSuccessful() && task.getResult() != null ){
                                databaseHelper.sendRegistrationToServer(task.getResult().getToken());
                            }
                            Intent intent = new Intent(StartActivity.this, MainHomeViewActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
        }

        Button backdoorSignIn = findViewById(R.id.buttonStartActivitySignInBackdoor);

        backdoorSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, AlternateSignInActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onClickSignInButton(View v) {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

}
