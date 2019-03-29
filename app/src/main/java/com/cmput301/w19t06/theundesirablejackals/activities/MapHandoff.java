package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.classes.FetchBook;
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

public class MapHandoff extends AppCompatActivity {
    private static final int BARCODE_PERMISSION_REQUEST = 1100;
    private static final int IMAGE_CAPTURE_REQUEST = 1101;
    private FirebaseVisionBarcodeDetectorOptions options;
    private Toolbar toolbar;
    private static final String TAG = "MapHandoffActivity";
    private String currentPhotoPath;
    private ArrayList<String> barcodesFound = new ArrayList<>();
    private BookRequest request;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_handoff);

        toolbar = findViewById(R.id.tool_barMapHandoff);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Book Hand-off");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        request = (BookRequest) intent.getSerializableExtra("info");

        EditText ISBNEditText = findViewById(R.id.editTextmapHandoffISBN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BorrowedListActivity.class));
                finish();
            }
        });

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.TYPE_ISBN)
                .build();

        TextView title = (TextView) findViewById(R.id.textViewHandoffBookTitle);
        TextView username = (TextView) findViewById(R.id.textViewHandoffOwner);
        TextView phone = (TextView) findViewById(R.id.textViewHandoffPhone);
        TextView email = (TextView) findViewById(R.id.textViewHandoffEmail);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
                ToastMessage.show(this, "Couldn't create temporary file for the scanner");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
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
     * @return A File object used to store the picture taken for barcode scanning
     * @throws IOException if the permissions are not correct or writing is prevented for some
     * reason the function will throw an IO Exception
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        File image;
        if(currentPhotoPath == null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            currentPhotoPath = image.getAbsolutePath();
        }else{image = new File(currentPhotoPath);}
        return image;
    }

    /**
     * called after successful completion of camera capture intent in order to scan for barcodes
     * in the photo
     */
    private void scanBarcode(){
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(this, contentUri);

            FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector(options);



            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            // Task completed successfully
                            checkForBarcodeData(barcodes);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            Log.e(TAG , e.toString());
                            //ToastMessage.show(this,"Failed to capture");
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
     * @param barcodes the List of firebase ML vision barcodes that were found
     */
    private void checkForBarcodeData(List<FirebaseVisionBarcode> barcodes){
        for (FirebaseVisionBarcode barcode: barcodes) {

            String rawValue = barcode.getRawValue();
            Log.d(TAG, rawValue);

            int valueType = barcode.getValueType();
            Log.d(TAG, valueType + " : " +  FirebaseVisionBarcode.TYPE_ISBN);
            // See API reference for complete list of supported types
            switch (valueType) {
                case FirebaseVisionBarcode.TYPE_ISBN:
                    //Add data to list
                    barcodesFound.add(rawValue);

                    ((TextView) findViewById(R.id.editTextmapHandoffISBN)).setText(barcodesFound.get(0));

                    Log.d(TAG, ((Integer)barcodesFound.size()).toString());
                    break;
                default:
                    ToastMessage.show(this,"No ISBN found");
                    break;
            }
        }
    }

    /**
     * Fetches image using gallery address provided in addPhotobtn
     * Also gets the photo taken by the ISBN scanner camera Intent
     * @param requestCode super : the code of the intent which made the request for result
     * @param resultCode super : the finish code of the activity that finished
     * @param data super : the data found on the finished intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
        if(requestCode == IMAGE_CAPTURE_REQUEST){
            if (resultCode == RESULT_OK) {

                scanBarcode();  //Tries to find barcodes and add them to barcodesFound arraylist object

            } else {
                ToastMessage.show(this,"Photo Scan Canceled");
            }
        }
    }

    /**
     * The activity onStop method, has been overridden to delete ISBN scanner pictures to
     * prevent taking up space on user's phone
     */
    @Override
    public void onStop(){
        super.onStop();
        if(currentPhotoPath != null) {
            File file = new File(currentPhotoPath);
            if (file.exists()) {
                if (file.delete()) {
                    Log.d(TAG, "file Deleted : " + currentPhotoPath);
                } else {
                    Log.d(TAG, "file not Deleted : " + currentPhotoPath);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== BARCODE_PERMISSION_REQUEST) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Scanner wont work without permission!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
