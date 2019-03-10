package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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
import com.cmput301.w19t06.theundesirablejackals.R;

public class MainHomeViewActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private Toolbar toolBar;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;
    private ListView listView;
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

        databaseHelper = new DatabaseHelper(MainHomeViewActivity.this);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //using the MenuItem passed, we can identify which item is selected by the user
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.addtnl_profile:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);

                        //close the drawer layout
                        drawerLayout.closeDrawers();

                        // go to "PersonalProfileActivity"
                        Intent intent_profile = new Intent(MainHomeViewActivity.this, PersonalProfileActivity.class);
                        startActivity(intent_profile);

                        return true;

                    case R.id.addtnl_notification:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        //displayMessage("Notifications selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        Intent intent3 = new Intent(MainHomeViewActivity.this, NotifcationActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.addtnl_borrowRqst:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        //displayMessage("Borrow Requests selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        Intent intent4 = new Intent(MainHomeViewActivity.this, BorrowedListActivty.class);
                        startActivity(intent4);
                        return true;

                    case R.id.addtnl_lendRqst:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        //displayMessage("Lend Requests selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        Intent intent5 = new Intent(MainHomeViewActivity.this, LentListActivity.class);
                        startActivity(intent5);
                        return true;

                    case R.id.addtnl_friends:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(MainHomeViewActivity.this, FriendsListActivity.class);
                        startActivity(intent2);
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
        //Adding Fragments
        adapter.AddFragment(new MyBooksFragment(),"My Books");
        adapter.AddFragment(new LibraryFragment(),"Library");
        adapter.AddFragment(new BorrowedFragment(),"Borrowed");
        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    // display the search icon on the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);

        return true;
    }

    //Check if the additional menu tabs are clickable
    public void displayMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void addBookButton(View view){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

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
}