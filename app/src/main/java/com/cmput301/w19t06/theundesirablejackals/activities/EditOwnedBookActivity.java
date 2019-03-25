package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditOwnedBookActivity extends AppCompatActivity {

    private final static String ACTIVITY_TAG = "EditOwnedBookActivity";
    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";

    public final static String EDIT_BOOK_OBJECT = "ToBeEditedBookObject";
    public final static String EDIT_BOOK_INFO = "ToBeEditedBookInfo";
    private static final int GALLERY_PERMISSION_REQUEST = 303;
    public static final int IMAGE_GALLERY_REQUEST = 300;

    private Toolbar mToolbar;

    private DatabaseHelper databaseHelper;

    private BookInformation mBookInformation;
    private Book mBookToBeEdited;

    private ImageView mImageViewEditBookPhoto;
    private TextView mTextViewISBN;
    private TextView mTextViewBookTitle;
    private TextView mTextViewAuthor;
    private EditText mTextViewCategories;
    private EditText mEdiTextDescription;

    private Button mButtonEditOwnedBookDone;
    private Button mButtonEditOwnedBookChoosePhoto;

    private Uri mImageUri;
    private String mCurrentPhotoPath;

    private boolean mFieldsEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owned_book);
        mToolbar = findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Editing owned book");
        setSupportActionBar(mToolbar);
        databaseHelper = new DatabaseHelper();
        Intent intent = getIntent();
        mBookToBeEdited = (Book) intent.getSerializableExtra(EDIT_BOOK_OBJECT);
        mBookInformation = (BookInformation) intent.getSerializableExtra(EDIT_BOOK_INFO);

        mImageViewEditBookPhoto = findViewById(R.id.imageViewEditOwnedBookPhoto);
        mTextViewISBN = findViewById(R.id.textViewEditOwnedBookISBN);
        mTextViewBookTitle = findViewById(R.id.textViewEditOwnedBookTitle);
        mTextViewAuthor = findViewById(R.id.textViewEditOwnedAuthor);
        mEdiTextDescription = findViewById(R.id.editTextEditOwnedBookDescription);

        mButtonEditOwnedBookDone = findViewById(R.id.buttonEditOwnedBookActivityDone);
        mButtonEditOwnedBookChoosePhoto = findViewById(R.id.buttonEditOwnedBookChoosePhoto);

        mTextViewBookTitle.setText(mBookToBeEdited.getTitle());
        mTextViewAuthor.setText(mBookToBeEdited.getAuthor());
        mTextViewISBN.setText("ISBN: " + mBookToBeEdited.getIsbn());
        mEdiTextDescription.setText(mBookInformation.getDescription());

        mFieldsEdited = false;

        setBookPhotoView();

        // back navigation button click listener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFieldsEdited) {
                    finish();
                } else {
                    warningChangesMade();
                }
            }
        });


        // set text watchers to look for changes
        mEdiTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mFieldsEdited = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (!mFieldsEdited) {
            finish();
        } else {
            warningChangesMade();
        }
    }

    public void OnClick_buttonEditOwnedBookChoosePhoto(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            dispatchImageGalleryIntent();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST);
        }

    }

    public void OnClick_textViewEditOwnedBookDeletePhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditOwnedBookActivity.this);
        builder.setMessage("Deleting book photo will set a default book photo. Do you want to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mImageViewEditBookPhoto.setImageResource(R.drawable.ic_book);
                        mImageUri = null;
                        mFieldsEdited = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Warning!");
        alert.show();
    }

    public void OnClick_buttonEditOwnedBookDone(View view) {

        // if no fields were changed job is done
        if (!mFieldsEdited) {
            finish();
            return;
        }

        mBookInformation.setDescription(mEdiTextDescription.getText().toString());


        ToastMessage.show(EditOwnedBookActivity.this, "SAVING TO DATABASE...");

        // new image was chosen, upload new image
        if (mImageUri != null) {
            mBookInformation.setBookPhoto(mImageUri.getLastPathSegment());
            databaseHelper.uploadBookPicture(mImageUri, mBookInformation, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if (bool) {
                        ToastMessage.show(EditOwnedBookActivity.this, "Picture uploaded to server!");
                    } else {
                        ToastMessage.show(EditOwnedBookActivity.this, "Sorry, something went wrong uploading picture");
                    }
                }
            });
        } else {
            mBookInformation.setBookPhoto(null);
        }

        databaseHelper.updateBookInformation(mBookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    //todo

                    Log.d(ACTIVITY_TAG, "Book information update success");
                } else {
                    //todo
                    Log.d(ACTIVITY_TAG, "Book information update failed");
                }
            }
        });

        Intent intent = new Intent(EditOwnedBookActivity.this, ViewOwnedBookActivity.class);
        intent.putExtra("description", mEdiTextDescription.getText().toString());
        intent.setData(mImageUri);
        setResult(ViewOwnedBookActivity.RESULT_OK, intent);
        finish();
    }

    private void dispatchImageGalleryIntent() {
        Intent photoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(ACTIVITY_TAG, ((Integer)requestCode).toString());
        Log.d(ACTIVITY_TAG, ((Integer)resultCode).toString());
        Log.d(ACTIVITY_TAG, (data).getDataString());

        if (requestCode == IMAGE_GALLERY_REQUEST) {
            Log.d(ACTIVITY_TAG, "We are inside the first IF");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(ACTIVITY_TAG, "We are inside the second IF");
                Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
                mImageUri = data.getData();
                mBookInformation.setBookPhotoByUri(mImageUri);
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(mImageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    mImageViewEditBookPhoto.setImageBitmap(image);
                    mFieldsEdited = true;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    ToastMessage.show(getApplicationContext(), "Unable to open image");
                }
            } else {
                ToastMessage.show(getApplicationContext(), "Add picture was canceled");
            }
        }
    }

    private void setBookPhotoView() {
        if (mBookInformation.getBookPhoto() == null) {
            mImageViewEditBookPhoto.setImageResource(R.drawable.ic_book);
            return;
        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = new File(storageDir, mBookInformation.getBookPhoto() + ".jpg");
        Uri photoData = Uri.fromFile(image);
        mImageViewEditBookPhoto.setImageURI(photoData);
        mImageUri = photoData;
    }

    private void warningChangesMade() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditOwnedBookActivity.this);
        builder.setMessage("Edited fields not saved. Do you wish to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Warning!");
        alert.show();
    }

}
