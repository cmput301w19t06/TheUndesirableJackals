package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Allows book borrowers to view a book they have borrowed, return the book, and view book owner's
 * profile.
 *
 * @author Art Limbaga
 */
public class ViewBorrowedBookActivity extends AppCompatActivity {
    private final static String ERROR_TAG_LOAD_IMAGE = "IMAGE_LOAD_ERROR";
    public final static String BORROWED_BOOK_FROM_RECYCLER_VIEW = "BorrowedBookFromRecyclerView";
    public final static String BORROWED_INFO_FROM_RECYCLER_VIEW = "InformationFromRecyclerView";

    private Toolbar mToolbar;

    private DatabaseHelper mDatabaseHelper;

    private Book mBorrowedBook;
    private BookInformation mBookInformation;

    private Button mButtonReturnBook;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mIsbn;
    private TextView mBookOwner;
    private TextView mDescription;
    private TextView mCategory;
    private ImageView mBookPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_borrowed_book);
        mToolbar =  findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle("Borrowed Book");
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mBorrowedBook = (Book) intent.getSerializableExtra(BORROWED_BOOK_FROM_RECYCLER_VIEW);
        mBookInformation = (BookInformation) intent.getSerializableExtra(BORROWED_INFO_FROM_RECYCLER_VIEW);

        mDatabaseHelper = new DatabaseHelper();

        mTitle = findViewById(R.id.textViewViewBorrowedBookBookTitle);
        mAuthor = findViewById(R.id.textViewViewBorrowedBookBookAuthor);
        mIsbn = findViewById(R.id.textViewViewBorrowedBookBookISBN);
        mDescription = findViewById(R.id.textViewViewBorrowedBookBookDescription);
        mBookOwner = findViewById(R.id.textViewViewBorrowedBookBookOwner);
        mCategory = findViewById(R.id.textViewViewBorrowedBookCategory);
        mButtonReturnBook = findViewById(R.id.buttonBorrowedBookReturnBook);
        mBookPhotoView = findViewById(R.id.imageViewViewBorrowedBookPhoto);


        mTitle.setText(mBorrowedBook.getTitle());
        mAuthor.setText(mBorrowedBook.getAuthor());
        mIsbn.setText("ISBN: " + mBorrowedBook.getIsbn());
        mBookOwner.setText("Owner: " + mBookInformation.getOwner());
        mCategory.setText("Category: "+ mBorrowedBook.getCategories());
        mDescription.setText(mBookInformation.getDescription());

        setBookPhotoView();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonReturnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMessage.show(getApplicationContext(), "Returning....");
            }
        });

        mBookOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBorrowedBookActivity.this, OthersProfileActivity.class);
                intent.putExtra(OthersProfileActivity.USERNAME, mBookInformation.getOwner());
                startActivity(intent);
            }
        });

    }

    private void setBookPhotoView() {

        if (mBookInformation.getBookPhoto() != null && !mBookInformation.getBookPhoto().isEmpty()) {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File image = new File(storageDir, mBookInformation.getBookPhoto() + ".jpg");
            if (!image.exists()) {
                mBookPhotoView.setImageResource(R.drawable.ic_loading_with_text);
                try {
                    mDatabaseHelper.downloadBookPicture(image, mBookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if (bool) {
                                if (image.exists()) {
                                    Uri photoData = Uri.fromFile(image);
                                    mBookPhotoView.setImageURI(photoData);
                                } else {
                                    ToastMessage.show(getApplicationContext(), "Something went quite wrong...");
                                }
                            } else {
                                ToastMessage.show(getApplicationContext(), "Photo not downloaded");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(ERROR_TAG_LOAD_IMAGE, e.getMessage());
                }
            } else {
                Uri photoData = Uri.fromFile(image);
                mBookPhotoView.setImageURI(photoData);
            }
        }
    }
}
