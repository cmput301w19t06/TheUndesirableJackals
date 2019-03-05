package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.cmput301.w19t06.theundesirablejackals.R;

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
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);
        Toast toast = new Toast(this);
        toast.setText("Sign up Button");
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
