package com.cmput301.w19t06.theundesirablejackals;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ArrayList<String> barcodes = new ArrayList<String>();
    private static final String TAG = "MainActivity";
    private TokenBroadcastReceiver mTokenReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        // Button click listeners
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.buttonScanBarcode).setOnClickListener(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, ((Integer)requestCode).toString());
        if (requestCode == SignInActivity.REQUEST_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                JSON user = data.getBundleExtra("UserDataKey");
////                JSON TO USER CLASS
//                Intent intent = new Intent(Library.class);



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

            }
        }
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
            startActivityForResult(intent, BarcodeDetect.REQUEST_BARCODE);

        }else if(i == R.id.buttonUpdate){
            if(barcodes.size() > 0) {
                ((TextView) findViewById(R.id.textViewBarcode)).setText(barcodes.get(0));
            }
        }
    }


}
