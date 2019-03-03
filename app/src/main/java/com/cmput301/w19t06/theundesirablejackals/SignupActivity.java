package com.cmput301.w19t06.theundesirablejackals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.User.User;

public class SignupActivity extends AppCompatActivity {

    public static final Integer MIN_PASSWORD_CHAR = 8;

    private EditText username, password, email, phone;
    private User newUser;
    private Button btn_signup_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.etext_signup_username);
        password = findViewById(R.id.etext_signup_password);
        email = findViewById(R.id.etext_signup_email);
        phone = findViewById(R.id.etext_signup_phone);


        btn_signup_confirm = findViewById(R.id.btn_signup_confirm);

        btn_signup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_phone = phone.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email)
                        || TextUtils.isEmpty(txt_phone) || TextUtils.isEmpty(txt_password)) {

                    showMessage("All fields required.");

                } else if (txt_password.length() < MIN_PASSWORD_CHAR) {

                    String message = "Password must be " + MIN_PASSWORD_CHAR.toString() + " characters long.";
                    showMessage(message);
                } else {
                    DatabaseHelper.getInstance(SignupActivity.this).createAccount(txt_username, txt_email, txt_password,txt_phone);
                }
            }
        });
    }

    public void showMessage(String text) {
        Toast message = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
    }
}

