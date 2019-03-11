/**
 * A pager adapter that represents each page as a view
 * The pages represented are My books, Library and Borrowed Fragments
 */


package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * implementation of a view pager that represents each page as a view
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitle = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * returns a fragment with an associated position
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * returns the number of views available
     * @return
     */
    @Override
    public int getCount() {
        return fragmentListTitle.size();
    }

    /**
     * A method called by the ViewPager to obtain a title string to describe the specified page
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) fragmentListTitle.get(position);
    }
    public void AddFragment(Fragment fragment, String Title){
        fragmentList.add(fragment);
        fragmentListTitle.add(Title);
    }
}
