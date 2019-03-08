package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.Database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.Database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.cmput301.w19t06.theundesirablejackals.User.UserInformation;

import java.util.HashMap;
import java.util.Map;

public class EditContactInfo extends AppCompatActivity {
    private Button submit;
    private Button cancel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        databaseHelper = new DatabaseHelper(this);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        // submit action
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // updates user
                getUser();
                // return to "PersonalProfileActivity
                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
                startActivity(intent);
            }
        });

        // cancel action
        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // return to "PersonalProfileActivity
                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getUser () {
        databaseHelper.getUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                updateUser(user);
            }
        });
    }

    public void updateUser(User user) {
        UserInformation userInfo = user.getUserinfo();

        // retrieve new email
        EditText email = (EditText) findViewById(R.id.editText);
        String newEmail = email.getText().toString();

        if (!newEmail.isEmpty()) {
            userInfo.setEmail(newEmail);
        }

        // retrieve new phone
        EditText phone = (EditText) findViewById(R.id.editText3);
        String newPhone = phone.getText().toString();

        if (!newPhone.isEmpty()) {
            userInfo.setPhoneNumber(newPhone);
        }

        // updates userInfo on branch "users" on Firebase
        Map<String, Object> uidMap = new HashMap<>();
        uidMap.put(
                user.getUserinfo().getUserName(),
                user.getUserinfo());
        databaseHelper.registerUserInfo(uidMap, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
            }
        });

        // updates object User on "registered/uid" on Firebase
        databaseHelper.saveCurrentUser(user, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
            }
        });
    }
}
