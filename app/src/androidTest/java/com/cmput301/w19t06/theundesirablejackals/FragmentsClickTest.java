package com.cmput301.w19t06.theundesirablejackals;

import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;

import com.cmput301.w19t06.theundesirablejackals.activities.AlternateSignInActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class FragmentsClickTest {

    @Rule
    public ActivityTestRule<AlternateSignInActivity> testRule =
            new ActivityTestRule<AlternateSignInActivity>(AlternateSignInActivity.class);

    @Before
    public void logIn() {
        // input text in the EditTexts
        Espresso.onView(withId(R.id.editTextAlternateSignInEmail)).perform(typeText("rdrgues@hotmail.com"));
        Espresso.onView(withId(R.id.editTextAlternateSignInPassword)).perform(typeText("password"));
        SystemClock.sleep(1500);
        // click the log in button
        Espresso.onView(withId(R.id.buttonAlternateSignIn)).perform(click());

        SystemClock.sleep(3000);
    }

    @Test
    public void myBooksFragment_isCorrect() {
        // switch to "my books" tab
        Espresso.onView(withId(R.id.tabLayoutMainHomeViewActivityFragmentTabs)).
                perform(selectTabAtPosition(0));

        SystemClock.sleep(3000);

        // clicks items in fragment
        // there are 4 books in this class we can iterate through
        for (Integer i = 0; i < 4; i++) {
            Espresso.onView(withId(R.id.myBooks_recyclerview)).
                    perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            SystemClock.sleep(1500);
            Espresso.pressBack();
            SystemClock.sleep(3000);
        }
    }

    // copied by Felipe on 2019-03-31 from:
    // https://stackoverflow.com/questions/49626315/how-to-select-a-specific-tab-position-in-tab-layout-using-espresso-testing
    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }

}