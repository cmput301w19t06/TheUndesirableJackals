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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.classes.FetchBook;
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

/**
 * This Activity adds books to a persons library. This is the main access to the two fragments
 * Author: Kaya Thiessen
 */

public class AddBookActivity extends AppCompatActivity {
    private FirebaseVisionBarcodeDetectorOptions options;
    public static final int IMAGE_GALLERY_REQUEST = 500;
    private static final int IMAGE_CAPTURE_REQUEST = 501;
    private static final int BARCODE_PERMISSION_REQUEST = 502;
    private static final int GALLERY_PERMISSION_REQUEST = 503;

    private static final String TAG = "AddBookActivity";


    private Uri imageUri;
    private String isbn;
    private String currentPhotoPath;
    private ArrayList<String> barcodesFound = new ArrayList<>();

    private ImageView chosenBookPhoto;


    /**
     * General Create
     * @param savedInstanceState The default onCreate method with it's data bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Toolbar mToolbar = findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Add Book");
        setSupportActionBar(mToolbar);

        chosenBookPhoto = findViewById(R.id.imageViewViewOwnedBookPhoto);
        EditText isbnEditText = findViewById(R.id.editTextAddBookBookISBN);

        //Set up options for the barcode scanner to add book by ISBN barcode
        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_EAN_13,
                                FirebaseVisionBarcode.TYPE_ISBN)
                        .build();

        // check for any change in isbnEditText to trigger search for additional details
        isbnEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    searchISBN();
                }
            }
        });

        //Set back button to stop this activity and go back to main home view
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainHomeViewActivity.class));
                finish();
            }
        });

    }

    /**
     * Executed when search by ISBN button is pressed
     * if the ISBN field contains a valid input and there is network connection, it will
     * fill the title, author and description fields for the user
     * version 1 - March 15, 2019
     */
    public void searchISBN() {
        // give the fields where the info is going to be placed as parameters
        EditText titleParam = findViewById(R.id.editTextAddBookBookTitle);
        EditText authorParam = findViewById(R.id.editTextAddBookBookAuthor);
        EditText descriptionParam = findViewById(R.id.editTextAddBookBookDescription);
        EditText isbnParam  = findViewById(R.id.editTextAddBookBookISBN);
        EditText categoriesParam = findViewById(R.id.editTextAddBookBookCategories);

        // retrieve the ISBN input by the user
        if(barcodesFound.size() > 0) {
            isbn = barcodesFound.get(0);
        }else{
            isbn = isbnParam.getText().toString();
        }

        // check internet network
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // begin search if there is connection and isbn is not empty
            if (networkInfo != null && networkInfo.isConnected() && isbn.length() != 0) {
                new FetchBook(titleParam, authorParam, descriptionParam, isbnParam, categoriesParam)
                        .execute(isbn);
            }
            // ad empty strings if something fails
            else {
                if (isbn.length() == 0) {
                    isbnParam.setText("");
                } else {
                    showMyToast("no network");
                }
            }
        }else{
            showMyToast("no network");
        }

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
     * Final add book button, sends data back to MainHomeViewActivity
     */
    public void OnClick_addBookDone(View view){
        EditText edit = findViewById(R.id.editTextAddBookBookTitle);
        String title = edit.getText().toString();
        edit = findViewById(R.id.editTextAddBookBookAuthor);
        String author = edit.getText().toString();
        edit = findViewById(R.id.editTextAddBookBookISBN);
        isbn = edit.getText().toString();
        edit = findViewById(R.id.editTextAddBookBookDescription);
        String description = edit.getText().toString();

        if (!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {

            Intent intent = new Intent();
            intent.putExtra("bookTitle", title);
            intent.putExtra("bookAuthor", author);
            intent.putExtra("bookIsbn", isbn);
            intent.putExtra("bookDescription", description);
            intent.setData(imageUri);
            setResult(MainHomeViewActivity.RESULT_OK, intent);
            finish();
        } else {
            showMyToast("Missing fields required!");
        }
    }

    /**
     * Used to create intent so info can be pushed back
     * @param context the context from which this activity is called
     * @return intent  the intent returned by this function meant to start this activity
     */
    public static Intent makeIntent(Context context){
        return new Intent(context,AddBookActivity.class);
    }

    /**
     * Add photos by using add photo button
     */
    public void onClick_ChooseBookPhoto(View view){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {

            dispatchImageGalleryIntent();

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST);
        }
    }

    /**
     * Creates and dispatches an intent to select an image from the gallery
     * which will be returned by getExtra on activity result
     */
    private void dispatchImageGalleryIntent(){
        Intent photoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
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
        if (requestCode== AddBookActivity.IMAGE_GALLERY_REQUEST){
            if(resultCode == RESULT_OK) {
                imageUri = data.getData();
                chosenBookPhoto.setImageURI(imageUri);

            }else{
                showMyToast("Add picture was canceled");
            }
        }else if(requestCode == IMAGE_CAPTURE_REQUEST){
            if (resultCode == RESULT_OK) {

                scanBarcode();  //Tries to find barcodes and add them to barcodesFound arraylist object


            } else {
                showMyToast("Photo Scan Canceled");
            }
        }
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
                            showMyToast("Failed to capture");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
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

                    ((TextView) findViewById(R.id.editTextAddBookBookISBN)).setText(barcodesFound.get(0));
                    searchISBN();

                    Log.d(TAG, ((Integer)barcodesFound.size()).toString());
                    break;
                default:
                    showMyToast("No ISBN found");
                    break;
            }
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
                showMyToast("Couldn't create temporary file for the scanner");
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
     * show a quick toast with the message provided
     * @param message the message to show in the toast
     */
    private void showMyToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
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
        }else if(requestCode == GALLERY_PERMISSION_REQUEST){
            if(grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchImageGalleryIntent();
            }else{
                Toast.makeText(this, "Can't add photos from storage!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}