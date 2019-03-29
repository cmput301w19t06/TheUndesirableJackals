package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
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
 * Allows use to accept of reject lend requests
 * Author: Kaya Thiessen
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends AppCompatActivity {
    private static final int BARCODE_PERMISSION_REQUEST = 450;
    private static final String TAG = "AcceptRejectLend";
    private static final int IMAGE_CAPTURE_REQUEST = 452;
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private boolean doneDatabaseFetch = false;
    private BookRequest request;
    private Geolocation geolocation;
    private String currentPhotoPath;
    private FirebaseVisionBarcodeDetectorOptions options;
    private ArrayList<String> barcodesFound = new ArrayList<>();

    /**
     * General creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_lend);

        Intent intent = getIntent();
        request = (BookRequest) intent.getSerializableExtra("info");

        Toolbar toolbar = findViewById(R.id.tool_barAcceptReject);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                doneDatabaseFetch = true;
                if(user != null) {
                    currentUser = user;
                    geolocation = user.getPickUpLocation();
                }
            }
        });

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.TYPE_ISBN)
                .build();


        TextView username = (TextView) findViewById(R.id.textViewAcceptRejectActivityUserRequesting);
        TextView phone = (TextView) findViewById(R.id.textViewAcceptRejectLendPhone);
        TextView email = (TextView) findViewById(R.id.textViewAcceptRejectLendEmail);
        final TextView title = (TextView) findViewById(R.id.textViewAcceptRejectActivityBookTitle);

        //Set the values
        username.setText(request.getBorrower().getUserName());
        phone.setText(request.getBorrower().getPhoneNumber());
        email.setText(request.getBorrower().getEmail());
        BookInformation i = request.getBookRequested();
        title.setText("");
        databaseHelper.getBookFromDatabase(i.getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                if(book != null){
                    title.setText(book.getTitle());
                }
            }
        });

        //Setting values to Visible depending on the status
        Button accept = (Button) findViewById(R.id.buttonAcceptRejectActivityAcceptRequest);
        Button reject = (Button) findViewById(R.id.buttonAcceptRejectActivityRejectRequest);
        LinearLayout map = (LinearLayout) findViewById(R.id.linearLayoutAcceptRejectLend);
        EditText isbn = (EditText) findViewById(R.id.editTextAcceptRejectLendISBN);
        Button isbnScan = (Button) findViewById(R.id.buttonAcceptRejectLendScanISBN);
        Button isbnConfirm = (Button) findViewById(R.id.buttonAcceptRejectLentConfirmISBN);

        BookRequestStatus status = request.getCurrentStatus();
        if (status == BookRequestStatus.PENDING){
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            map.setVisibility(View.INVISIBLE);
            isbn.setVisibility(View.INVISIBLE);
            isbnScan.setVisibility(View.INVISIBLE);
            isbnConfirm.setVisibility(View.INVISIBLE);
        }
        else if(status == BookRequestStatus.ACCEPTED){
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            isbn.setVisibility(View.VISIBLE);
            isbnScan.setVisibility(View.VISIBLE);
            isbnConfirm.setVisibility(View.VISIBLE);
        }

        else if(status == BookRequestStatus.HANDED_OFF){
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            isbn.setVisibility(View.VISIBLE);
            isbnScan.setVisibility(View.VISIBLE);
            isbnConfirm.setVisibility(View.VISIBLE);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity.class));
                finish();
            }
        });

    }

    /**
     * If the accept button is pressed
     * @param view
     */
    public void accept(View view){
    //TODO
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_location_popup_alert);
        dialog.setCanceledOnTouchOutside(true);
        TextView titleView = (TextView) dialog.findViewById(R.id.textViewAcceptRejectLendPopupTitle);
        titleView.setText("Use Default Pickup Location?");

        Button yes = (Button) dialog.findViewById(R.id.buttonAcceptRejectLendPopupChangeLocationYes);
        Button no = (Button) dialog.findViewById(R.id.buttonAcceptRejectLendPopupChangeLocationNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when send button is clicked
                if(doneDatabaseFetch && currentUser != null) {
                    sendRequestUpdate(BookRequestStatus.ACCEPTED);
                }else if(doneDatabaseFetch){
                    showToast("Something went wrong communicating with database");
                }
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when NO button is clicked
                retrieveLocation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void sendRequestUpdate(final BookRequestStatus status){
        request.setCurrentStatus(status);
        request.setPickuplocation(geolocation);
        databaseHelper.updateLendRequest(request, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    if(status == BookRequestStatus.DENIED){
                        doDeniedStuff();
                    }else {
                        doAcceptedStuff();
                    }
                } else {
                    showToast("Something went wrong updating the request");
                }
            }
        });
    }

    private void doAcceptedStuff() {
        databaseHelper.getSpecificBookLendRequests(request.getBookRequested().getOwner(),
                request.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if(bookRequestList != null && bookRequestList.size() > 0){
                            for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                if(bookRequest.getBookRequestLendKey().equals(request.getBookRequestLendKey())){
                                    continue;
                                }
                                bookRequest.setCurrentStatus(BookRequestStatus.DENIED);
                                databaseHelper.updateLendRequest(bookRequest, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if(bool){
                                            showToast("Request updated successfully");
                                        }else{
                                            showToast("Something went wrong updating all other requests");
                                        }
                                    }
                                });
                            }
                        }else if(bookRequestList == null){
                            showToast("Something went wrong updating the book status");
                        }else{
                            showToast("You shouldn't ever see this message...");
                        }
                    }
                });
        BookInformation bookInformation = request.getBookRequested();
        bookInformation.setStatus(BookStatus.ACCEPTED);
        databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    showToast("Book status updated successfully");
                }else{
                    showToast("Book status wasn't updated");
                }
            }
        });
    }

    private void doDeniedStuff(){
        databaseHelper.getSpecificBookLendRequests(request.getBookRequested().getOwner(),
                request.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if(bookRequestList != null && bookRequestList.size() > 0){
                            boolean noOpenRequests = true;
                            for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                if(bookRequest.getCurrentStatus() != BookRequestStatus.DENIED){
                                    noOpenRequests = false;
                                }
                            }
                            if(noOpenRequests) {
                                BookInformation bookInformation = request.getBookRequested();
                                bookInformation.setStatus(BookStatus.AVAILABLE);
                                databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            showToast("Request updated successfully");
                                        } else {
                                            showToast("Couldn't update book status to AVAILABLE");
                                        }
                                    }
                                });
                            }
                        }else if(bookRequestList == null){
                            showToast("Something went wrong updating the book status");
                        }else{
                            showToast("You shouldn't ever see this message...");
                        }
                    }
                });
    }

    /**
     * Calls "PersonalProfileActivity" to allow the user to select a location on the map
     */
    public void retrieveLocation() {
        Intent i = new Intent(this, SelectLocationActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Triggers after the activity for result of "PersonalProfileActivity" is completed
     * Will save the returned result on "longitude" and "latitude" attributes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                // the coordinates to do whatever you need to do with them
                geolocation.setLatitude(Double.parseDouble(data.getStringExtra("lat")));
                geolocation.setLongitude(Double.parseDouble(data.getStringExtra("lng")));
                sendRequestUpdate(BookRequestStatus.ACCEPTED);
            }
        }

        if(requestCode == IMAGE_CAPTURE_REQUEST){
            if (resultCode == RESULT_OK) {

                scanBarcode();  //Tries to find barcodes and add them to barcodesFound arraylist object

            } else {
                ToastMessage.show(this,"Photo Scan Canceled");
            }
        }

        // continue here
//        Intent intent = new Intent();
//        intent.putExtra("resultAD",true);
//        setResult(Activity.RESULT_OK,intent);
//        finish();
    }

    /**
     * If the reject button is pressed
     * @param view
     */
    public void reject(View view){
        //TODO
        //Return False, If False delete only this request
        sendRequestUpdate(BookRequestStatus.DENIED);
    }


    private void showToast(String message){
        ToastMessage.show(this, message);
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

                    ((TextView) findViewById(R.id.editTextAcceptRejectLendISBN)).setText(barcodesFound.get(0));

                    Log.d(TAG, ((Integer)barcodesFound.size()).toString());
                    break;
                default:
                    ToastMessage.show(this,"No ISBN found");
                    break;
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
