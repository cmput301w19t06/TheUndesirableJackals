package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.AddBookCameraFragment;
import com.cmput301.w19t06.theundesirablejackals.AddBookManualFragment;
import com.cmput301.w19t06.theundesirablejackals.R;

import java.io.File;

/*
 * Created by Kaya on 02/02/2019
 * */

public class AddBookActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_view);
        tabLayout = (TabLayout) findViewById(R.id.addbooktablayout_id);
        viewPager = (ViewPager) findViewById(R.id.addbook_viewpage_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new AddBookCameraFragment(), "ISBN Scanner");
        adapter.AddFragment(new AddBookManualFragment(), "Edit");

        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    public void finalAddbookbtn(View view){
        String title, author, isbn, description;
        EditText edit = (EditText)findViewById(R.id.booktitleAdd_id);
        title = edit.getText().toString();
        edit = (EditText)findViewById(R.id.bookauthorAdd_id);
        author = edit.getText().toString();
        edit = (EditText)findViewById(R.id.isbnAdd_id);
        isbn = edit.getText().toString();
        edit = (EditText)findViewById(R.id.descriptionAdd_id);
        description = edit.getText().toString();

        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
    public void addPhotobtn(View view){
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");

        startActivity(photoIntent);

    }
}