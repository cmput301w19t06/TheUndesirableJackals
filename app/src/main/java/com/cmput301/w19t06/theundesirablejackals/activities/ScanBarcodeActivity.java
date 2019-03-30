package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanBarcodeActivity extends AppCompatActivity {

    private static final int BARCODE_PERMISSION_REQUEST = 802;
    private static final int IMAGE_CAPTURE_REQUEST = 801;

    public static final String SCAN_BARCODE = "ScanBarcode";

    private static final String TAG = "ScanBarcodeActivity";

    private String mImageFilePath;
    private Uri mImageUri;


    private ArrayList<String> mBarcodesFound = new ArrayList<>();
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
        isbnReader();
    }

    /**
     * Allows access to ISBN reader
     * Either gets permission or prevents activation of camera if the permission is not granted
     */
    public void isbnReader() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            dispatchTakePictureIntent();

        } else {
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
                ToastMessage.show(getApplicationContext(), "Couldn't create temporary file for the scanner");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "Starting taking barcode photo");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cmput301.w19t06.theundesirablejackals",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
            }
        }
    }

    /**
     * Create a temporary file to store the picture being requests for the barcode scanner
     *
     * @return A File object used to store the picture taken for barcode scanning
     * @throws IOException if the permissions are not correct or writing is prevented for some
     *                     reason the function will throw an IO Exception
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        File image;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mImageFilePath = image.getAbsolutePath();
        Log.d(TAG, "Creating file success");
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ScanBarcodeActivity.IMAGE_CAPTURE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Barcode image retrieved");
                searchImageForBarCode();

            } else {
                ToastMessage.show(getApplicationContext(), "Scanning barcode was canceled");
                finish();
            }
        }
    }

    /**
     * called after successful completion of camera capture intent in order to scan for barcodes
     * in the photo
     */
    private void searchImageForBarCode() {
        File f = new File(mImageFilePath);
        Uri contentUri = Uri.fromFile(f);

        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(this, contentUri);

            FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector(mDetectorOptions);


            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            // Task completed successfully
                            Log.d(TAG, "Detecting for bar done");
                            checkForBarcodeData(barcodes);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            Log.e(TAG, e.toString());
                            ToastMessage.show(getApplicationContext(), "Failed to capture");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Passed a list of barcodes found by the Firebase ML vision scanner this will extract the
     * ISBN's found
     *
     * @param barcodes the List of firebase ML vision barcodes that were found
     */
    private void checkForBarcodeData(List<FirebaseVisionBarcode> barcodes) {
        for (FirebaseVisionBarcode barcode : barcodes) {

            String rawValue = barcode.getRawValue();
            Log.d(TAG, rawValue);

            int valueType = barcode.getValueType();
            Log.d(TAG, valueType + " : " + FirebaseVisionBarcode.TYPE_ISBN);
            // See API reference for complete list of supported types
            if (valueType == FirebaseVisionBarcode.TYPE_ISBN) {
                //Add data to list
                Log.d(TAG, "ISBN Barcode type detected");
                mBarcodesFound.add(rawValue);
                Log.d(TAG, ((Integer) mBarcodesFound.size()).toString());
                continue;
            }
        }
        returnToCaller();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mImageFilePath != null) {
            File file = new File(mImageFilePath);
            if (file.exists()) {
                if (file.delete()) {
                    Log.d(TAG, "file Deleted : " + mImageFilePath);
                } else {
                    Log.d(TAG, "file not Deleted : " + mImageFilePath);
                }
            }
        }
    }

    private void returnToCaller() {
        Log.d(TAG, "Returning to caller");
        Intent intent;

        if (this.getCallingActivity().getClassName().equals(ViewAcceptedLendRequestActivity.class.getName())) {
            intent = new Intent(ScanBarcodeActivity.this, ViewAcceptedLendRequestActivity.class);
            if (mBarcodesFound.size() > 0) {
                intent.putExtra("ISBN", mBarcodesFound.get(0));
                setResult(RESULT_OK, intent);
            }
        }
        if (this.getCallingActivity().getClassName().equals(ViewHandedoffBookRequestActivity.class.getName())) {
            intent = new Intent(ScanBarcodeActivity.this, ViewHandedoffBookRequestActivity.class);
            if (mBarcodesFound.size() > 0) {
                intent.putExtra("ISBN", mBarcodesFound.get(0));
                setResult(RESULT_OK, intent);
            }
        }
        // add more if statements when a new caller wants ISBN scanned
        finish();
    }


}
