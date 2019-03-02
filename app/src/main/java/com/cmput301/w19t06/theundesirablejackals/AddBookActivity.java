package com.cmput301.w19t06.theundesirablejackals;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

/*
 * Created by Kaya on 02/02/2019
 * */

public class AddBookActivity extends AppCompatActivity {
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
}