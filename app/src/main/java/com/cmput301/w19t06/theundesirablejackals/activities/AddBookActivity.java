package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This Activity adds books to a persons library. This is the main access to the two fragments
 * Author: Kaya Thiessen
 */

public class AddBookActivity extends AppCompatActivity {
    private FirebaseVisionBarcodeDetectorOptions options;
    public static final int IMAGE_GALLERY_REQUEST = 5;
    private static final int IMAGE_CAPTURE = 301;
    public static final String BARCODES_DATA_CODE = "BarCode";
    private String TAG = "AddBookActivity";
    private Button buttonAddBook;
    private Button buttonAddPhoto;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    private Uri imageUri;
    private String title, author, isbn, description;
    private String currentPhotoPath;
    private ArrayList<String> barcodesFound = new ArrayList<String>();

    /**
     * General Create
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_manual);

        buttonAddBook = (Button) findViewById(R.id.buttonAddBookActivityAddBook);
        buttonAddPhoto = (Button) findViewById(R.id.buttonAddBookActivityAddPhotos);

        options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_EAN_13)
                        .build();

//        tabLayout = (TabLayout) findViewById(R.id.addbooktablayout_id);
//        viewPager = (ViewPager) findViewById(R.id.addbook_viewpage_id);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
//        adapter.AddFragment(new AddBookCameraFragment(), "ISBN Scanner");
//        adapter.AddFragment(new AddBookManualFragment(), "Edit");
//
//        //adapter setup
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * Allows access to ISBN reader
     * @param view
     */
    public void isbnReader(View view){
        dispatchTakePictureIntent();
    }

    /**
     * Final add book button, sends data back to MainHomeViewActivity
     * @param view
     */
    public void finalAddbookbtn(View view){
        EditText edit = (EditText)findViewById(R.id.editTextAddBookBookTitle);
        title = edit.getText().toString();
        edit = (EditText)findViewById(R.id.editTextAddBookBookAuthor);
        author = edit.getText().toString();
        edit = (EditText)findViewById(R.id.editTextAddBookBookISBN);
        isbn = edit.getText().toString();
        edit = (EditText)findViewById(R.id.editTextAddBookBookDescription);
        description = edit.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("bookTitle", title);
        intent.putExtra("bookAuthor", author);
        intent.putExtra("bookIsbn", isbn);
        intent.putExtra("bookDescription", description);
        intent.setData(imageUri);
        setResult(MainHomeViewActivity.RESULT_OK, intent);
        finish();

    }

    /**
     * Used to create intent so info can be pushed back
     * @param context
     * @return intent
     */
    public static Intent makeIntent(Context context){
        return new Intent(context,AddBookActivity.class);
    }

    /**
     * Add photos by using add photo button
     * @param view
     */
    public void addPhotobtn(View view){
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    /**
     * Fetches image using gallery address provided in addPhotobtn
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
        if (requestCode== AddBookActivity.IMAGE_GALLERY_REQUEST){
            if(resultCode == RESULT_OK) {
                imageUri = data.getData();
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    showMyToast("Unable to open image");
                }
            }else{
                showMyToast("Add picture was canceled");
            }
        }else if(requestCode == IMAGE_CAPTURE){
            if (resultCode == RESULT_OK) {

//                galleryAddPic();
                scanBarcode();  //Tries to find barcodes and add them to barcodesFound arraylist object
                if(barcodesFound.size() > 0){
                    ((TextView) findViewById(R.id.editTextAddBookBookISBN)).setText(barcodesFound.get(0));
                }else{
                    showMyToast("ISBN Not Found. Please try again");
                }

            } else {
                showMyToast("Photo Scan Canceled");
            }

        }

    }


//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }


    private void scanBarcode(){
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(this, contentUri);

            FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector(options);
//                .getVisionBarcodeDetector();


            Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
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
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File image;
        if(currentPhotoPath == null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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

        // Save a file: path for use with ACTION_VIEW intents

    }


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
                    Log.d(TAG, ((Integer)barcodesFound.size()).toString());
                    break;
                default:
                    showMyToast("No ISBN found");
                    break;
            }
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cmput301.w19t06.theundesirablejackals",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE);

            }
        }
    }


    private void showMyToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }

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
}