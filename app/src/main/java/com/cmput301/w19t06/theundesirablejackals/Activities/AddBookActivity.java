package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.Adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.AddBookCameraFragment;
import com.cmput301.w19t06.theundesirablejackals.AddBookManualFragment;
import com.cmput301.w19t06.theundesirablejackals.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/*
 * Created by Kaya on 02/02/2019
 * */

public class AddBookActivity extends AppCompatActivity {
    public static final int IMAGE_GALLERY_REQUEST = 5;
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

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
        if (requestCode==RESULT_OK){
            if (requestCode==IMAGE_GALLERY_REQUEST){
                Uri imageUri = data.getData();
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}