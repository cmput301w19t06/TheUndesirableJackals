package com.cmput301.w19t06.theundesirablejackals.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditOwnedBookActivity extends AppCompatActivity {

    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";

    public final static String EDIT_BOOK_OBJECT = "ToBeEditedBookObject";
    public final static String EDIT_BOOK_INFO = "ToBeEditedBookInfo";
    private static final int GALLERY_PERMISSION_REQUEST = 303;
    public static final int IMAGE_GALLERY_REQUEST = 300;

    private Toolbar mToolbar;

    private DatabaseHelper databaseHelper;

    private BookInformation mBookInformation;
    private Book mBookToBeEdited;

    private ImageView   mImageViewEditBookPhoto;
    private EditText    mEdiTextEditISBN;
    private EditText    mEditTextBookTitle;
    private EditText    mEditTextAuthor;
    private EditText    mEdiTextEditCategories;
    private EditText    mEdiTextDescription;

    private Button  mButtonEditOwnedBookDone;
    private Button  mButtonEditOwnedBookChoosePhoto;

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

        Intent intent = getIntent();
        mBookToBeEdited = (Book) intent.getSerializableExtra(EDIT_BOOK_OBJECT);
        mBookInformation = (BookInformation) intent.getSerializableExtra(EDIT_BOOK_INFO);

        mImageViewEditBookPhoto = findViewById(R.id.imageViewEditOwnedBookPhoto);
        mEdiTextEditISBN = findViewById(R.id.editTextEditOwnedBookISBN);
        mEditTextBookTitle = findViewById(R.id.editTextEditOwnedBookTitle);
        mEditTextAuthor = findViewById(R.id.editTextEditOwnedBookAuthor);
        mEdiTextEditCategories = findViewById(R.id.editTextEditOwnedBookCategories);
        mEdiTextDescription = findViewById(R.id.editTextEditOwnedBookDescription);

        mButtonEditOwnedBookDone = findViewById(R.id.buttonEditOwnedBookActivityDone);
        mButtonEditOwnedBookChoosePhoto = findViewById(R.id.buttonEditOwnedBookChoosePhoto);

        mEditTextBookTitle.setText(mBookToBeEdited.getTitle());
        mEditTextAuthor.setText(mBookToBeEdited.getAuthor());
        mEdiTextEditISBN.setText(mBookToBeEdited.getIsbn());
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
        mEditTextBookTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mFieldsEdited = true;
            }
        });
        mEditTextAuthor.addTextChangedListener(new TextWatcher() {
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
        mEdiTextEditISBN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mFieldsEdited = true;
            }
        });
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
        warningChangesMade();
    }

    public void OnClick_buttonEditOwnedBookChoosePhoto(View view){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            dispatchImageGalleryIntent();

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST);
        }

    }

    public void OnClick_buttonEditOwnedBookDone(View view) {
        String newTitle = mEditTextBookTitle.getText().toString();
        String newAuthor = mEditTextAuthor.getText().toString();
        String newIsbn = mEdiTextEditISBN.getText().toString();
        String newDescription = mEdiTextDescription.getText().toString();
        ToastMessage.show(EditOwnedBookActivity.this, "SAVING TO DATABASE...");

//        if (!newTitle.isEmpty() && !newAuthor.isEmpty() && !newIsbn.isEmpty()) {
//
//            Intent intent = new Intent();
//            intent.putExtra("bookTitle", newTitle);
//            intent.putExtra("bookAuthor", newAuthor);
//            intent.putExtra("bookIsbn", newIsbn);
//            intent.putExtra("bookDescription", newDescription);
//            intent.setData(mImageUri);
//            setResult(MainHomeViewActivity.RESULT_OK, intent);
//            finish();
//
//            databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
//                @Override
//                public void onCallback(User user) {
//                    BookInformation bookInformation = MainHomeViewActivity.update(user, imageUri, isbn, description);
//
//                    databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
//                        @Override
//                        public void onCallback(boolean bool) {
//                            if(bool){
//                                //todo
//                                Log.d(TAG, "All good in update book information");
//                            }else{
//                                //todo
//                                Log.d(TAG, "NOT good in update book information");
//                            }
//                        }
//                    });
//                    Boolean check = false;
//
//                    for(Book b :ownedBooksAdapter.getDataSet().getBookList().getBooks()) {
//                        if(b.getIsbn().equals(isbn)){
//                            check = true;
//                            break;
//                        }
//                    }
//                    if(!check) {
//                        ownedBooksAdapter.addItem(book, bookInformation);
//                        user.getOwnedBooks().addBook(book.getIsbn(), bookInformation.getBookInformationKey());
//                        databaseHelper.updateOwnedBooks(user.getOwnedBooks(), new BooleanCallback() {
//                            @Override
//                            public void onCallback(boolean bool) {
//                                if (bool) {
//                                    displayMessage("Saved your book on server");
//                                } else {
//                                    displayMessage("Didn't manage to save your book to the server");
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        } else {
//            ToastMessage.show(getApplicationContext(),"Missing fields required!");
//        }
    }

    private void dispatchImageGalleryIntent(){
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
        if (requestCode == AddBookActivity.IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                mImageUri = data.getData();
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(mImageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    mImageViewEditBookPhoto.setImageURI(mImageUri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    ToastMessage.show(getApplicationContext(),"Unable to open image");
                }
            } else {
                ToastMessage.show(getApplicationContext(),"Add picture was canceled");
            }
        }
    }

    private void setBookPhotoView() {

        if (mBookInformation.getBookPhoto() != null && !mBookInformation.getBookPhoto().isEmpty()) {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File image = new File(storageDir, mBookInformation.getBookPhoto() + ".jpg");
            if(!image.exists()) {
                mImageViewEditBookPhoto.setImageResource(R.drawable.ic_loading_with_text);
                try {
                    databaseHelper.downloadBookPicture(image, mBookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if(bool){
                                if(image.exists()){
                                    Uri photoData = Uri.fromFile(image);
                                    mImageViewEditBookPhoto.setImageURI(photoData);
                                    Log.d("ViewBookActiv", "image now exists... COOL");
                                }else{
                                    ToastMessage.show(getApplicationContext(),"Something went quite wrong...");
                                }
                            }else{
                                ToastMessage.show(getApplicationContext(), "Photo not downloaded");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(ERROR_TAG_LOAD_IMAGE, e.getMessage());
                }
            }else{
                Uri photoData = Uri.fromFile(image);
                mImageViewEditBookPhoto.setImageURI(photoData);
                Log.d("ViewBookActiv", "image already exists... COOL");
            }
        }
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
