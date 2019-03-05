package com.cmput301.w19t06.theundesirablejackals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301.w19t06.theundesirablejackals.Database.DatabaseHelper;

public class MainHomeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_view);
//        DatabaseHelper databaseHelper = new DatabaseHelper(MainHomeViewActivity.this);
//        databaseHelper.getUserFromDatabase();
    }
}