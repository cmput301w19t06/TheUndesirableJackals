package com.cmput301.w19t06.theundesirablejackals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);
        btnSignUp = (Button) findViewById(R.id.signupInitial_id);
    }
    public void finalSignupBtn(View view){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
}

