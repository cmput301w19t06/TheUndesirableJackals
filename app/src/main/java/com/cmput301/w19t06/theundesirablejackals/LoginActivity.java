package com.cmput301.w19t06.theundesirablejackals;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.L;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
//    EditText email, password;
//    Button btn_login, btn_signup;
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // check if someone is already logged in on the current running device
////        if(DatabaseHelper.getInstance(LoginActivity.this).isUserLoggedin()) {
////            Intent intent = new Intent(LoginActivity.this, MainHomeViewActivity.class);
////            startActivity(intent);
////            finish();
////        }
//
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
//
//
//        email = findViewById(R.id.etext_login_email);
//        password = findViewById(R.id.etext_login_password);
//
//        btn_login = findViewById(R.id.btn_login);
//        btn_signup = findViewById(R.id.btn_login_signup);
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt_email = email.getText().toString();
//                String txt_password = password.getText().toString();
//
//                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
//                    Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
//                } else {
////                    DatabaseHelper.getInstance(LoginActivity.this).login(txt_email, txt_password);
//
//                }
//            }
//        });
//
//        btn_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, SignupActivity.class ));
//                finish();
//            }
//        });
//    }
}
