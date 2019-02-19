package com.cmput301.w19t06.theundesirablejackals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignUp = (Button) findViewById(R.id.signupInitial_id);

    }


    public void loginBtn(View view) {
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);

    }

    public void signupBtn(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
