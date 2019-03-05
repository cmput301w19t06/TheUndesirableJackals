package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cmput301.w19t06.theundesirablejackals.Activities.AddBookActivity;
import com.cmput301.w19t06.theundesirablejackals.Adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.Fragment.BorrowedFragment;
import com.cmput301.w19t06.theundesirablejackals.Fragment.LibraryFragment;
import com.cmput301.w19t06.theundesirablejackals.Fragment.MyBooksFragment;
import com.cmput301.w19t06.theundesirablejackals.R;

public class MainHomeViewActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private Toolbar toolBar;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_view);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        //toolBar = findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //using the MenuItem passed, we can identify which item is selected by the user
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.addtnl_profile:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        displayMessage("Profile selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addtnl_notification:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        displayMessage("Notifications selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addtnl_borrowRqst:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        displayMessage("Borrow Requests selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addtnl_lendRqst:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        displayMessage("Lend Requests selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.addtnl_friends:
                        //highlight the selected item/tab
                        menuItem.setChecked(true);
                        displayMessage("Friends selected....");
                        //close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
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

    //Check if the additional menu tabs are clickable
    public void displayMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void addBookButton(View view){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
}