package com.cmput301.w19t06.theundesirablejackals;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class BarcodeDetect extends AppCompatActivity {
    private FirebaseVisionBarcodeDetectorOptions options;
    private static final int REQUEST_IMAGE_CAPTURE = 321;
    public static final int REQUEST_BARCODE = 100;
    private Bitmap imageBitmap;
    private ArrayList<String> barcodesFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_EAN_13)
                        .build();
        scanBarcode();
    }


    private void scanBarcode() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    private void scanBarcodePII(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

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
                        Log.e("Image Capture Failed: ", e.toString());
                        showMyToast("Failed to capture");
                    }
                });
    }

    private void checkForBarcodeData(List<FirebaseVisionBarcode> barcodes){
        for (FirebaseVisionBarcode barcode: barcodes) {

            String rawValue = barcode.getRawValue();

            int valueType = barcode.getValueType();
            // See API reference for complete list of supported types
            switch (valueType) {
                case FirebaseVisionBarcode.TYPE_ISBN:
                    //Add data to list
                    barcodesFound.add(rawValue);
                    break;
                default:
                    showMyToast("No ISBN found");
                    break;
            }
        }
    }

    private void showMyToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE){

            Intent _result = new Intent();

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                scanBarcodePII();  //Tries to find barcodes and add them to barcodesFound arraylist object

                if(barcodesFound.size() > 0){

                    _result.putStringArrayListExtra("barcodes", barcodesFound);
                    setResult(Activity.RESULT_OK, _result);

                }
            } else {

                setResult(Activity.RESULT_CANCELED, _result);

            }
            finish();
        }
    }
}
