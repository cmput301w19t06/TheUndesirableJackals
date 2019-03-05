package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.R;

public class PersonalProfileActivity extends AppCompatActivity {
    private Button editProfilebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editProfilebtn = (Button) findViewById(R.id.profileEditBtn_id);
    }
    public void editProfilebtnPress(View view){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
}
