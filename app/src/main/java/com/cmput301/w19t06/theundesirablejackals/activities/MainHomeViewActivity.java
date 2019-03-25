/**
 * The First Activity that is launched when the user signs in the app
 * it contains three Fragments. "My Books", "Library" and "Borrowed" Fragment and a hidden
 * menu
 * @Version 1 - Jan - 2019
 * @see MyBooksFragemt, LibraryFragment, BorrowedFragment
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.fragment.BorrowedFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.LibraryFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.MyBooksFragment;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainHomeViewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public static final String TAG = "MainHomeViewActivity";

    public static final int ADD_BOOK = 50;
    private TabLayout tabLayout;
    private Toolbar toolBar;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseHelper databaseHelper;
    private BooksRecyclerViewAdapter ownedBooksAdapter = new BooksRecyclerViewAdapter();
    private BooksRecyclerViewAdapter libraryBooksAdapter = new BooksRecyclerViewAdapter();
    private BooksRecyclerViewAdapter borrowedBooksAdapter = new BooksRecyclerViewAdapter();

    private TextView mDrawerUsername;
    private TextView mDrawerEmail;

    /**
     * Creates a tablayout for the fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_view);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutMainHomeViewActivityFragmentTabs);
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigationViewMainHomeViewActivity);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //enable home button on the app i.e it adds home button to the app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        databaseHelper = new DatabaseHelper();

        setDrawerUserInfo();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            /**
             * using the MenuItem passed, we can identify which item is selected by the user
             * @param menuItem the selected item
             * @return true to display the item as the selected item
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                /* highlight the selected item/tab */
                menuItem.setChecked(true);
                //close the drawer layout
                drawerLayout.closeDrawers();
                Intent intent;
                boolean bool = true;
                switch (menuItem.getItemId()){
                    case R.id.itemMenuProfile:
                        intent = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
                        break;
                    case R.id.itemMenuBorrowRequests:
                        intent = new Intent(MainHomeViewActivity.this, BorrowedListActivity.class);
                        break;
                    case R.id.itemMenuLendRequests:
                        intent = new Intent(MainHomeViewActivity.this, LentListActivity.class);
                        break;
                    case R.id.itemMenuFriends:
                        intent = new Intent(MainHomeViewActivity.this, FriendsListActivity.class);
                        break;
                    case R.id.itemMenuSearchUser:
                        intent = new Intent(MainHomeViewActivity.this, OthersProfileActivity.class);
                        break;
                    case R.id.itemMenuLogout:
                        intent = new Intent(MainHomeViewActivity.this, StartActivity.class);
                        databaseHelper.signOut();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                    default:
                        intent = new Intent();
                        bool = false;
                        break;
                }
                if(bool){
                    startActivity(intent);
                }
                return bool;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpagerMainHomeViewActivity);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /* Adding Fragments */
        adapter.AddFragment(new MyBooksFragment(),"My Books");
        adapter.AddFragment(new LibraryFragment(),"Library");
        adapter.AddFragment(new BorrowedFragment(),"Borrowed");

        /* adapter setup */
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     *  displays the search icon on the app bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mDrawerUsername = findViewById(R.id.textViewMenuUsername);
        mDrawerEmail = findViewById(R.id.textViewMenuEmail);

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    public void setDrawerUserInfo() {
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                // retrieve user's info
                UserInformation userInformation = user.getUserInfo();
                String userName = userInformation.getUserName();
                String email = userInformation.getEmail();


                // display the info
                TextView usernameView = (TextView) findViewById(R.id.textViewMenuUsername);
                usernameView.setText(userName);

                TextView emailView = (TextView) findViewById(R.id.textViewMenuEmail);
                emailView.setText(email);

                ImageView profilePhoto = findViewById(R.id.imageViewMenuProfile);

                    // TODO: Change image to profile photo from database
                //profilePhoto.setImageResource(R.drawable.default_profile_photo);

            }
        });
    }

    public void setOwnedBooksAdapter(BooksRecyclerViewAdapter adapter){
        this.ownedBooksAdapter = adapter;
    }

    public void setLibraryBooksAdapter(BooksRecyclerViewAdapter adapter){
        this.libraryBooksAdapter = adapter;
    }

    public void setBorrowedBooksAdapter(BooksRecyclerViewAdapter adapter){
        this.borrowedBooksAdapter = adapter;
    }

    /**
     * Checks if the additional menu tabs are clickable
     * @param message to indicate which item is se
     */
    public void displayMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Add book Button for my books
     * @param view
     * Author: Kaya Thiessen
     */
    public void OnClick_AddOwnedBookButton(View view){
        Intent intent = AddBookActivity.makeIntent(MainHomeViewActivity.this);
        startActivityForResult(intent, ADD_BOOK);
    }

    public void OnClick_ProfileImage(View view) {
        Intent intent = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * To open Navigation Drawer when user tabs the menu icon
     * @param item a menu item
     * @return true for selecting the icon
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // open navigation drawer by tabbing the menu icon
        switch (item.getItemId()){

            case android.R.id.home:
                  drawerLayout.openDrawer(GravityCompat.START);
                  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * Author Kaya Thiessen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_BOOK:
                if (resultCode == RESULT_OK) {
                    final String title = data.getStringExtra("bookTitle");
                    final String author = data.getStringExtra("bookAuthor");
                    final String isbn = data.getStringExtra("bookIsbn");
                    final String description = data.getStringExtra("bookDescription");
                    final String categories = data.getStringExtra("categories");
                    final String thumbnail = data.getStringExtra("thumbnail");
                    final Uri imageUri = data.getData();
//                    InputStream inputStream;

                    final Book book = new Book(title, author, isbn, categories, thumbnail);

                    databaseHelper.addBookToDatabase(book, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if(bool){
                                Log.d(TAG, "Book sent to server");
                            }else{
                                Log.d(TAG, "Sorry, something went wrong :(");
                            }
                        }
                    });
                    databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
                        @Override
                        public void onCallback(User user) {
                            BookInformation bookInformation = updatebookInformation(user, imageUri,
                                    isbn, description, categories, thumbnail);

                            databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                @Override
                                public void onCallback(boolean bool) {
                                    if(bool){
                                        //todo
                                        Log.d(TAG, "All good in update book information");
                                    }else{
                                        //todo
                                        Log.d(TAG, "NOT good in update book information");
                                    }
                                }
                            });
                            Boolean check = false;

                            for(Book b :ownedBooksAdapter.getDataSet().getBookList().getBooks()) {
                                if(b.getIsbn().equals(isbn)){
                                    check = true;
                                    break;
                                }
                            }
                            if(!check) {
                                ownedBooksAdapter.addItem(book, bookInformation);
                                user.getOwnedBooks().addBook(book.getIsbn(), bookInformation.getBookInformationKey());
                                databaseHelper.updateOwnedBooks(user.getOwnedBooks(), new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            displayMessage("Saved your book on server");
                                        } else {
                                            displayMessage("Didn't manage to save your book to the server");
                                        }
                                    }
                                });
                            }
                        }
                    });
//                    try {
//                        inputStream = getContentResolver().openInputStream(imageUri);
//                        Bitmap image = BitmapFactory.decodeStream(inputStream);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
//                    }
                }
                break;
        }
    }



    public BookInformation updatebookInformation(User user, Uri imageUri, String isbn, String description, String categories, String thumbnail) {
        BookInformation bookInformation;
        if (user != null && user.getOwnedBooks() != null && user.getOwnedBooks().getBooks() != null) {
            if (!user.getOwnedBooks().getBooks().containsKey(isbn)) {
                if (imageUri != null) {
                    bookInformation = new BookInformation(
                            BookStatus.AVAILABLE,
                            imageUri,
                            description,
                            isbn,
                            user.getUserInfo().getUserName(),
                            categories,
                            thumbnail);

                    databaseHelper.uploadBookPicture(imageUri, bookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if (bool) {
                                //todo
                                displayMessage("Picture uploaded to server!");
                            } else {
                                //todo
                                displayMessage("Sorry, something went wrong uploading picture");
                            }
                        }
                    });
                } else {
                    bookInformation = new BookInformation(BookStatus.AVAILABLE, description, isbn,
                            user.getUserInfo().getUserName(), categories, thumbnail);
                }
            } else if (user.getOwnedBooks().getBooks().containsKey(isbn)) {
                if (imageUri != null) {
                    bookInformation = new BookInformation(
                            BookStatus.AVAILABLE,
                            imageUri,
                            description,
                            isbn,
                            user.getUserInfo().getUserName(),
                            categories,
                            thumbnail);
                    bookInformation.setBookInformationKey(user.getOwnedBooks().get(isbn));
                    databaseHelper.uploadBookPicture(imageUri, bookInformation, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if (bool) {
                                //todo
                                displayMessage("Picture uploaded to server!");
                            } else {
                                //todo
                                displayMessage("Sorry, something went wrong uploading picture");
                            }
                        }
                    });
                } else {
                    bookInformation = new BookInformation(BookStatus.AVAILABLE, description, isbn,
                            user.getUserInfo().getUserName(), categories, thumbnail);
                    bookInformation.setBookInformationKey(user.getOwnedBooks().get(isbn));
                }

            } else {
                bookInformation = new BookInformation(isbn, user.getUserInfo().getUserName());
            }
        }else{bookInformation = new BookInformation();}
        return bookInformation;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        String userInput = s.toLowerCase();
        List<String> listItem = new ArrayList<>();



        return false;
    }
}