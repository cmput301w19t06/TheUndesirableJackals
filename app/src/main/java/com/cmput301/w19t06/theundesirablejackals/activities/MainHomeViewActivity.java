/**
 * The First Activity that is launched when the user signs in the app
 * it contains three Fragments. "My Books", "Library" and "Borrowed" Fragment and a hidden
 * menu
 *
 * @Version 1 - Jan - 2019
 * @see MyBooksFragemt, LibraryFragment, BorrowedFragment
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.fragment.BorrowedFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.LibraryFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.MyBooksFragment;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

public class MainHomeViewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
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
    private MenuItem mSelectedFilter;

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
                switch (menuItem.getItemId()) {
                    case R.id.itemMenuProfile:
                        intent = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
                        break;
                    case R.id.itemMenuMessages:
                        intent = new Intent(MainHomeViewActivity.this, MessagesActivity.class);
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
                    case R.id.itemMenuFriendRequests:
                        intent = new Intent();
                        bool = false;
                        break;
                    case R.id.itemMenuDefaultPickupLocation:
                        intent = new Intent(MainHomeViewActivity.this, MapsActivity.class);
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
                if (bool) {
                    startActivity(intent);
                }
                return bool;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpagerMainHomeViewActivity);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /* Adding Fragments */
        adapter.AddFragment(new MyBooksFragment(), "My Books");
        adapter.AddFragment(new LibraryFragment(), "Library");
        adapter.AddFragment(new BorrowedFragment(), "Borrowed");

        /* adapter setup */
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    /**
     * displays the search icon on the app bar
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

                setProfilePhotoInDrawer(user);

                // TODO: Change image to profile photo from database
                //profilePhoto.setImageResource(R.drawable.default_profile_photo);

            }
        });
    }

    public void setOwnedBooksAdapter(BooksRecyclerViewAdapter adapter) {
        this.ownedBooksAdapter = adapter;
    }

    public void setLibraryBooksAdapter(BooksRecyclerViewAdapter adapter) {
        this.libraryBooksAdapter = adapter;
    }

    public void setBorrowedBooksAdapter(BooksRecyclerViewAdapter adapter) {
        this.borrowedBooksAdapter = adapter;
    }

    /**
     * Checks if the additional menu tabs are clickable
     *
     * @param message to indicate which item is se
     */
    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Add book Button for my books
     *
     * @param view Author: Kaya Thiessen
     */
    public void OnClick_AddOwnedBookButton(View view) {
        Intent intent = AddBookActivity.makeIntent(MainHomeViewActivity.this);
        startActivityForResult(intent, ADD_BOOK);
    }

    public void OnClick_ProfileImage(View view) {
        Intent intent = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(intent);
    }

    /**
     * To open Navigation Drawer when user tabs the menu icon
     *
     * @param item a menu item
     * @return true for selecting the icon
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BookInformationPairing filteredItem = new BookInformationPairing();
        BookInformationPairing toFilterThrough = ownedBooksAdapter.getDataCopy();
        String menuTitle;
        // open navigation drawer by tabbing the menu icon

        //initialize list of all filter menu items


        switch (item.getItemId()) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // Handle available status selection
            case R.id.itemFilterMenuAvailable:
                // get the status the user wants to filter the book list

                menuTitle = item.getTitle().toString().toUpperCase();
                Log.d(TAG, menuTitle);
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                ownedBooksAdapter.setDataSet(filteredItem);

                //ToastMessage.show(MainHomeViewActivity.this, "Filtering by AVAILABLE");
                break;
            // Handle requested status selection
            case R.id.itemFilterMenuRequested:
                // get the status the user wants to filter the book list
                menuTitle = item.getTitle().toString().toUpperCase();
                Log.d(TAG, menuTitle);
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                ownedBooksAdapter.setDataSet(filteredItem);

                //ToastMessage.show(MainHomeViewActivity.this, "Filtering by REQUESTED");
                break;
            // Handle accepted status selection
            case R.id.itemFilterMenuAccepted:
                // get the status the user wants to filter the book list
                menuTitle = item.getTitle().toString().toUpperCase();
                Log.d(TAG, menuTitle);
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                ownedBooksAdapter.setDataSet(filteredItem);

                //ToastMessage.show(MainHomeViewActivity.this, "Filtering by ACCEPTED");
                break;

            // Handle borrowed status selection
            case R.id.itemFilterMenuBorrowed:
                // get the status the user wants to filter the book list
                menuTitle = item.getTitle().toString().toUpperCase();
                Log.d(TAG, menuTitle);
                filterThrough(filteredItem,toFilterThrough,menuTitle);
                ownedBooksAdapter.setDataSet(filteredItem);

                //ToastMessage.show(MainHomeViewActivity.this, "Filtering by BORROWED");
                break;
        }

        if(item.equals(mSelectedFilter)) {
            mSelectedFilter = null;
            item.setChecked(false);
            ownedBooksAdapter.setDataSet(ownedBooksAdapter.getDataCopy());
        } else {
            item.setChecked(true);
            mSelectedFilter = item;
        }
        return super.onOptionsItemSelected(item);

    }


    /**
     * Resets the filter menu options every new choice
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for(int i=0; i<menu.size(); i++){
            if(menu.getItem(i).equals(mSelectedFilter)) {
                continue;
            } else {
                menu.getItem(i).setChecked(false);
            }
        }
        return true;
    }


    /**
     * Filters the list of books visible on My Books tab by status
     * @param filteredItem a collection of book list with a specific status specified by a user
     * @param toFilterThrough the bookInformationPairing class
     * @param menuTitle status to be used when filtering the books
     */
    // filters through books by status
    private void filterThrough(BookInformationPairing filteredItem, BookInformationPairing toFilterThrough, String menuTitle) {
        for(int i = 0; i < toFilterThrough.size(); i++){
            //get the status of the book
            String status = toFilterThrough.getInformation(i).getStatus().toString();
            Log.d(TAG, "I'm inside the filterthrough method");
            Log.d(TAG, status);
            Log.d(TAG, menuTitle);

            if(status.equals(menuTitle)){
                Log.d(TAG,"I'm inside the if statement");
                filteredItem.addPair(toFilterThrough.getBook(i), toFilterThrough.getInformation(i));
            }
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data        Author Kaya Thiessen
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
                            if (bool) {
                                Log.d(TAG, "Book sent to server");
                            } else {
                                Log.d(TAG, "Sorry, something went wrong :(");
                            }
                        }
                    });
                    databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
                        @Override
                        public void onCallback(User user) {
                            BookInformation bookInformation = updatebookInformation(user, imageUri,
                                    isbn, description);

                            databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                @Override
                                public void onCallback(boolean bool) {
                                    if (bool) {
                                        //todo
                                        Log.d(TAG, "All good in update book information");
                                    } else {
                                        //todo
                                        Log.d(TAG, "NOT good in update book information");
                                    }
                                }
                            });
                            Boolean check = false;

                            for (Book b : ownedBooksAdapter.getDataSet().getBookList().getBooks()) {
                                if (b.getIsbn().equals(isbn)) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
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


    public BookInformation updatebookInformation(User user, Uri imageUri, String isbn, String description) {
        BookInformation bookInformation;
        if (user != null && user.getOwnedBooks() != null && user.getOwnedBooks().getBooks() != null) {
            if (!user.getOwnedBooks().getBooks().containsKey(isbn)) {
                if (imageUri != null) {
                    bookInformation = new BookInformation(
                            BookStatus.AVAILABLE,
                            imageUri,
                            description,
                            isbn,
                            user.getUserInfo().getUserName());

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
                            user.getUserInfo().getUserName());
                }
            } else if (user.getOwnedBooks().getBooks().containsKey(isbn)) {
                if (imageUri != null) {
                    bookInformation = new BookInformation(
                            BookStatus.AVAILABLE,
                            imageUri,
                            description,
                            isbn,
                            user.getUserInfo().getUserName());
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
                            user.getUserInfo().getUserName());
                    bookInformation.setBookInformationKey(user.getOwnedBooks().get(isbn));
                }

            } else {
                bookInformation = new BookInformation(isbn, user.getUserInfo().getUserName());
            }
        } else {
            bookInformation = new BookInformation();
        }
        return bookInformation;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        String userInput = s.toLowerCase();
        BookInformationPairing listItem = new BookInformationPairing();
        // figures out which fragment a user is viewing
        switch (tabLayout.getSelectedTabPosition()) {

            // if User is viewing MyBooks tab
            case 0:
                //get the adapter data of MyBooks Fragment
                BookInformationPairing toSearchThrough = ownedBooksAdapter.getDataCopy();
                // search through the data
                searchThrough(toSearchThrough, userInput, listItem);
                // set the adapter data to the list of books that match the user input
                // in this case, the recyclerview adapter shrinks and
                // it only shows books matching the search result
                ownedBooksAdapter.setDataSet(listItem);
                break;
            // User is viewing Library tab
            case 1:
                //get the adapter data of Library Fragment
                toSearchThrough = libraryBooksAdapter.getDataCopy();
                // search through the data
                searchThrough(toSearchThrough, userInput, listItem);
                // set the adapter data to the list of books that match the user input
                // in this case, the recyclerview adapter shrinks and
                // it only shows books matching the search result
                libraryBooksAdapter.setDataSet(listItem);
                break;
            // User is viewing Library tab
            case 2:
                //get the adapter data of Library Fragment
                toSearchThrough = borrowedBooksAdapter.getDataCopy();
                // search through the data
                searchThrough(toSearchThrough, userInput, listItem);
                // set the adapter data to the list of books that match the user input
                // in this case, the recyclerview adapter shrinks and
                // it only shows books matching the search result
                borrowedBooksAdapter.setDataSet(listItem);
                break;
        }

        return true;
    }

    /**
     * this function searches through a list of books
     * and compares the userInput to the title,isbn, author of each book
     * if the title/isbn/author matches the user input, that specific book is added to listItem
     *
     * @param toSearchThrough the bookInformationPairing class
     * @param userInput       string characters
     * @param listItem        initially it's an empty bookInformationPairing list.
     * @return listItem
     */
    private void searchThrough(BookInformationPairing toSearchThrough,String userInput, BookInformationPairing listItem) {
        for (int i = 0; i < toSearchThrough.size();i++){

            String isbn = toSearchThrough.getBook(i).getIsbn();
            String author = toSearchThrough.getBook(i).getAuthor();
            String title = toSearchThrough.getBook(i).getTitle();
            if (isbn.toLowerCase().contains(userInput)) {

                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));

            } else if (author.toLowerCase().contains(userInput)) {
                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));
            } else if (title.toLowerCase().contains(userInput)) {
                listItem.addPair(toSearchThrough.getBook(i), toSearchThrough.getInformation(i));
            }

        }
//    return listItem;
    }

    private void setProfilePhotoInDrawer(User user) {
        UserInformation userinfo = user.getUserInfo();
        final ImageView drawerProfilePhoto = findViewById(R.id.imageViewMenuProfile);
        if (userinfo.getUserPhoto() != null && !userinfo.getUserPhoto().isEmpty()) {
            databaseHelper.getProfilePictureUri(userinfo, new UriCallback() {
                @Override
                public void onCallback(Uri uri) {
                    if (uri !=null) {
                        Picasso.get()
                                .load(uri)
                                .error(R.drawable.ic_person_outline_grey_24dp)
                                .placeholder(R.drawable.ic_loading_with_text)
                                .into(drawerProfilePhoto);
                    }
                }
            });
        }}
    
}