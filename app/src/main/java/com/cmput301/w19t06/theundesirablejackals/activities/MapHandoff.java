package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.classes.FetchBook;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

import java.util.ArrayList;

public class MapHandoff extends AppCompatActivity {
    private FirebaseVisionBarcodeDetectorOptions options;
    private Toolbar toolbar;
    private ArrayList<String> barcodesFound = new ArrayList<>();
    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_handoff);

        toolbar = findViewById(R.id.tool_barMapHandoff);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Book Hand-off");
        setSupportActionBar(toolbar);

        EditText ISBNEditText = findViewById(R.id.editTextmapHandoffISBN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BorrowedListActivity.class));
                finish();
            }
        });
    }


}
