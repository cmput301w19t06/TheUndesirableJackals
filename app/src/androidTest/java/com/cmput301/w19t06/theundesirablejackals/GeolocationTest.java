package com.cmput301.w19t06.theundesirablejackals;

/**
 * Log in, main menu and Google maps API testing
 * Will simply logs ins and select the "my pickup location" option on the menu
 * Will display the default pick up location of the user
 * For test account it will be at the U of A
 * @Version March 31, 2019
 */

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.cmput301.w19t06.theundesirablejackals.activities.AlternateSignInActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
public class GeolocationTest {

    @Rule
    public ActivityTestRule<AlternateSignInActivity> testRule =
            new ActivityTestRule<AlternateSignInActivity>(AlternateSignInActivity.class);

    @Before
    public void logIn() {
        // input text in the EditTexts
        Espresso.onView(withId(R.id.editTextAlternateSignInEmail))
                .perform(typeText("rdrgues@hotmail.com"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.editTextAlternateSignInPassword))
                .perform(typeText("password"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(1500);
        // click the log in button
        Espresso.onView(withId(R.id.buttonAlternateSignIn)).perform(click());

        SystemClock.sleep(3000);
    }

    @Test
    public void geolocation_isCorrect() {
        Espresso.onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(1500);

        // click the pick up location request option
        Espresso.onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuDefaultPickupLocation));

        SystemClock.sleep(3000);
        Espresso.onView(withId(R.id.buttonCancel)).perform(click());
        SystemClock.sleep(1500);
    }

}