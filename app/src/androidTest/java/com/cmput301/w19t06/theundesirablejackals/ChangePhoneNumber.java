package com.cmput301.w19t06.theundesirablejackals;

/**
 * Log in, main menu, user profile and edit profile testing
 * Will navigate trough the sequence until it reaches the option to change phone number
 * It will input a new phone number and it should be updated on the database
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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChangePhoneNumber {

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
    public void changePhone() {
        // open main menu
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(1500);

        // open profile
        onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuProfile));

        SystemClock.sleep(1500);

        // open menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        SystemClock.sleep(1500);
        onView(withText("Edit Phone Number")).perform(click());
        SystemClock.sleep(1500);

        // input the new phone number and press "ok"
        onView(withId(R.id.editTextPrompNewPhoneNumberInput)).perform(typeText("7809155324"));
        onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        SystemClock.sleep(4000);
    }

}