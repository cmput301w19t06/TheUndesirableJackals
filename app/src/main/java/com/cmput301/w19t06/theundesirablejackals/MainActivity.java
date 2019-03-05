package com.cmput301.w19t06.theundesirablejackals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ArrayList<String> barcodes = new ArrayList<String>();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        // Button click listeners
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.buttonScanBarcode).setOnClickListener(this);
        findViewById(R.id.buttonUpdate).setOnClickListener(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, ((Integer)requestCode).toString());
        if (requestCode == SignInActivity.REQUEST_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                database = FirebaseDatabase.getInstance();
//                myRef = database.getReference("users/" + currentUser.getUid());
//
//                myRef.setValue("Hello, World!");
//
//                mStorageRef = FirebaseStorage.getInstance().getReference();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
        else if(requestCode == BarcodeDetect.REQUEST_BARCODE){
            if(resultCode == RESULT_OK){
                barcodes = data.getStringArrayListExtra(BarcodeDetect.BARCODES_DATA_CODE);
                Log.d(TAG, ((Integer)barcodes.size()).toString());
                if(barcodes.size() > 0) {
                    ((TextView) findViewById(R.id.textViewBarcode)).setText(barcodes.get(0));
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
//            signIn();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);

        }else if(i == R.id.buttonScanBarcode){
            Intent intent = new Intent(MainActivity.this, BarcodeDetect.class);
//            intent.putStringArrayListExtra(BarcodeDetect.BARCODES_DATA_CODE, barcodes);
//            intent.setFlags()
            startActivityForResult(intent, BarcodeDetect.REQUEST_BARCODE);

        }else if(i == R.id.buttonUpdate){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    showMyToast("Sign out complete");
//                    finish();

                }
            });

        }
    }


    private void showMyToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }

}
