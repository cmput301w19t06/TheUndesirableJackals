package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanBarcodeActivity extends AppCompatActivity {

    private static final int BARCODE_PERMISSION_REQUEST = 802;
    private static final int IMAGE_CAPTURE_REQUEST = 801;

    private static final String TAG = "ScanBarcodeActivity";

    private FirebaseVisionBarcodeDetectorOptions mDetectorOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        //Set up options for the barcode scanner to add book by ISBN barcode
        mDetectorOptions = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.TYPE_ISBN)
                .build();
    }

    /**
     * Allows access to ISBN reader
     * Either gets permission or prevents activation of camera if the permission is not granted
     */
    public void isbnReader(View view){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {

            dispatchTakePictureIntent();

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    BARCODE_PERMISSION_REQUEST);
        }
    }

    /**
     * Creates an intent to take a picture and store the picture in the provided file
     */
    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Log.e(TAG, ex.getMessage());
//                ToastMessage.show(getApplicationContext(),"Couldn't create temporary file for the scanner");
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.cmput301.w19t06.theundesirablejackals",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
//            }
//        }
    }

    /**
     * Create a temporary file to store the picture being requests for the barcode scanner
     * @return A File object used to store the picture taken for barcode scanning
     * @throws IOException if the permissions are not correct or writing is prevented for some
     * reason the function will throw an IO Exception
     */
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        File image;
//        if(currentPhotoPath == null) {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
//            String imageFileName = "JPEG_" + timeStamp + "_";
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            image = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".jpg",         /* suffix */
//                    storageDir      /* directory */
//            );
//            currentPhotoPath = image.getAbsolutePath();
//        }else{image = new File(currentPhotoPath);}
//        return image;
//    }


}
