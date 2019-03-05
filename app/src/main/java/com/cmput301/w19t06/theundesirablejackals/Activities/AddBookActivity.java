package com.cmput301.w19t06.theundesirablejackals.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.Adapter.ViewPagerAdapter;
import com.cmput301.w19t06.theundesirablejackals.AddBookCameraFragment;
import com.cmput301.w19t06.theundesirablejackals.AddBookManualFragment;
import com.cmput301.w19t06.theundesirablejackals.R;

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
    public void finalAddbookbtn(View view){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
    public void addPhotobtn(View view){
    }
}