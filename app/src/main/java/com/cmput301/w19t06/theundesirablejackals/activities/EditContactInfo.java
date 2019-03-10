package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

public class EditContactInfo extends AppCompatActivity {
    private Button submit;
    private Button cancel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        databaseHelper = new DatabaseHelper();
        databaseHelper.getUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                ((EditText) findViewById(R.id.editText3)).setText(user.getUserInfo().getPhoneNumber());
//                updateUser(user);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        // submit action
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // updates user
                getUser();
                // return to "PersonalProfileActivity
//                Intent intent = new Intent(view.getContext(), MainHomeViewActivity.class);
//                startActivity(intent);
            }
        });

        // cancel action
        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // return to "PersonalProfileActivity
                startMainHomeView();
            }
        });
    }

    public void getUser () {
        databaseHelper.getUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
//                ((EditText) findViewById(R.id.editText)).setText(user.getUserInfo().getEmail());
//                ((EditText) findViewById(R.id.editText3)).setText(user.getUserInfo().getPhoneNumber());
                updateUser(user);
            }
        });
    }

    public void updateUser(User user) {
        UserInformation userInfo = user.getUserInfo();
        // retrieve new phone
        EditText phone = (EditText) findViewById(R.id.editText3);
        String newPhone = phone.getText().toString();

        if (!newPhone.isEmpty()) {
            userInfo.setPhoneNumber(newPhone);
        }

        // updates userInfo on branch "users" on Firebase
        databaseHelper.updateUserInfo(userInfo, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    startMainHomeView();
                }
            }
        });

//        // updates object User on "registered/uid" on Firebase
//        databaseHelper.saveCurrentUser(user, new BooleanCallback() {
//            @Override
//            public void onCallback(boolean bool) {
//            }
//        });
    }

    public void startMainHomeView(){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
}
