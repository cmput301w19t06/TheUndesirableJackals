package com.cmput301.w19t06.theundesirablejackals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {
    EditText username, password, email, phone;
    Button btn_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.edit_text_signup_username);
        password = findViewById(R.id.edit_text_signup_password);
        email = findViewById(R.id.edit_text_signup_email);
        phone = findViewById(R.id.edit_text_signup_phone);
    }

    public void OnClickSignupButton(View view) {

    }
}
