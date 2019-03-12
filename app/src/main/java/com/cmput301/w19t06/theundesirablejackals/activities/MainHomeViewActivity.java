/**
 * The First Activity that is launched when the user signs in the app
 * it contains three Fragments. "My Books", "Library" and "Borrowed" Fragment and a hidden
 * menu
 * @Version 1 - Jan - 2019
 * @see MyBooksFragemt, LibraryFragment, BorrowedFragment
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.cmput301.w19t06.theundesirablejackals.adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.fragment.BorrowedFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.LibraryFragment;
import com.cmput301.w19t06.theundesirablejackals.fragment.MyBooksFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainHomeViewActivity extends AppCompatActivity {
    public static final int ADD_BOOK = 50;
    private TabLayout tabLayout;
    private Toolbar toolBar;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;
    private ListView listView;

    /**
     * Creates a tablayout for the fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_view);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //enable home button on the app i.e it adds home button to the app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        databaseHelper = new DatabaseHelper();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            /**
             * using the MenuItem passed, we can identify which item is selected by the user
             * @param menuItem the selected item
             * @return true to display the item as the selected item
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.addtnl_profile:
                        /* highlight the selected item/tab */
                        menuItem.setChecked(true);
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
                        startActivity(intent1);
                        return true;

                    case R.id.addtnl_notification:
                        /* highlight the selected item/tab */
                        menuItem.setChecked(true);
                        //displayMessage("Notifications selected....");
                        /*close the drawer layout*/
                        drawerLayout.closeDrawers();
                        Intent intent3 = new Intent(MainHomeViewActivity.this, NotificationActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.addtnl_borrowRqst:
                        /* highlight the selected item/tab */
                        menuItem.setChecked(true);
                        //displayMessage("Borrow Requests selected....");
                        /* close the drawer layout */
                        drawerLayout.closeDrawers();
                        Intent intent4 = new Intent(MainHomeViewActivity.this, BorrowedListActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.addtnl_lendRqst:
                        /* highlight the selected item/tab */
                        menuItem.setChecked(true);
                        //displayMessage("Lend Requests selected....");
                        /* close the drawer layout */
                        drawerLayout.closeDrawers();
                        Intent intent5 = new Intent(MainHomeViewActivity.this, LentListActivity.class);
                        startActivity(intent5);
                        return true;

                    case R.id.addtnl_friends:
                        /* highlight the selected item/tab */
                        menuItem.setChecked(true);
                        /* close the drawer layout */
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(MainHomeViewActivity.this, FriendsListActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.addtnl_search:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);

                        //close the drawer layout
                        drawerLayout.closeDrawers();

                        Intent intent_search = new Intent(MainHomeViewActivity.this, BookSearch.class);
                        startActivity(intent_search);
                        return true;

                    case R.id.addtn1_logout:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(MainHomeViewActivity.this, StartActivity.class);
                        databaseHelper.signOut();
                        startActivity(intent);
                        finish();
                }


                return false;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
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

        getMenuInflater().inflate(R.menu.search_menu,menu);

        return true;
    }

    /**
     * Checks if the additional menu tabs are clickable
     * @param message to indicate which item is se
     */
    public void displayMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Add book Button for my books
     * @param view
     * Author: Kaya Thiessen
     */
    public void addBookButton(View view){
        Intent intent = AddBookActivity.makeIntent(MainHomeViewActivity.this);
        startActivityForResult(intent, ADD_BOOK);
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
        switch (resultCode) {
            case ADD_BOOK:
                if (resultCode == MainHomeViewActivity.RESULT_OK) {
                    String title = data.getStringExtra("bookTitle");
                    String author = data.getStringExtra("bookAuthor");
                    String isbn = data.getStringExtra("bookIsbn");
                    String description = data.getStringExtra("bookDescription");
                    Uri imageUri = data.getData();
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