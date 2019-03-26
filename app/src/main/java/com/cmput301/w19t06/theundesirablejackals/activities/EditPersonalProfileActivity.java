package com.cmput301.w19t06.theundesirablejackals.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditPersonalProfileActivity extends AppCompatActivity {
    public final static String USER_INFORMATION_OBJECT = "UserInformationObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_profile);
    }
}
