package com.cmput301.w19t06.theundesirablejackals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    public void showMessage(String text) {
        Toast message = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
    }
}

