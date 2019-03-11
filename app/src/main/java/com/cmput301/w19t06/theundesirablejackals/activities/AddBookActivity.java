package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Context;
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

import com.cmput301.w19t06.theundesirablejackals.adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.AddBookCameraFragment;
import com.cmput301.w19t06.theundesirablejackals.AddBookManualFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This Activity adds books to a persons library. This is the main access to the two fragments
 * Author: Kaya Thiessen
 */

public class AddBookActivity extends AppCompatActivity {
    public static final int IMAGE_GALLERY_REQUEST = 5;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Uri imageUri;
    private String title, author, isbn, description;

    /**
     * General Create
     * @param savedInstanceState
     */
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

    /**
     * Allows access to ISBN reader
     * @param view
     */
    public void isbnReader(View view){
        Intent intent = new Intent(this, BarcodeDetectActivity.class);
        startActivity(intent);
    }

    /**
     * Final add book button, sends data back to MainHomeViewActivity
     * @param view
     */
    public void finalAddbookbtn(View view){
        EditText edit = (EditText)findViewById(R.id.booktitleAdd_id);
        title = edit.getText().toString();
        edit = (EditText)findViewById(R.id.bookauthorAdd_id);
        author = edit.getText().toString();
        edit = (EditText)findViewById(R.id.isbnAdd_id);
        isbn = edit.getText().toString();
        edit = (EditText)findViewById(R.id.descriptionAdd_id);
        description = edit.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("bookTitle", title);
        intent.putExtra("bookAuthor", author);
        intent.putExtra("bookIsbn", isbn);
        intent.putExtra("bookDescription", description);
        intent.setData(imageUri);
        setResult(MainHomeViewActivity.RESULT_OK, intent);
        finish();

    }

    /**
     * Used to create intent so info can be pushed back
     * @param context
     * @return intent
     */
    public static Intent makeIntent(Context context){
        return new Intent(context,AddBookActivity.class);
    }

    /**
     * Add photos by using add photo button
     * @param view
     */
    public void addPhotobtn(View view){
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    /**
     * Fetches image using gallery address provided in addPhotobtn
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
        if (requestCode==RESULT_OK){
            if (requestCode== AddBookActivity.IMAGE_GALLERY_REQUEST){
                imageUri = data.getData();
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