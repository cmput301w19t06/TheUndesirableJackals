package com.cmput301.w19t06.theundesirablejackals;

/**
 * Log in, main menu and borrow/lend request testing
 * Will navigate through the main menu and select the options to display borrow and lend requests
 * For the testing account it will contain 0-4 and 2 books respectively
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
public class BorrowLendRequestsTest {

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
    public void borrowRequest_isCorrect() {
        openMainMenu();

        // click the borrow request option
        Espresso.onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuBorrowRequests));

        SystemClock.sleep(1500);
        Espresso.pressBack();
        SystemClock.sleep(1500);
    }

    @Test
    public void LendRequests_isCorrect() {
        openMainMenu();

        // click the borrow request option
        Espresso.onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuLendRequests));

        SystemClock.sleep(1500);
        Espresso.pressBack();
        SystemClock.sleep(1500);
    }

    private void openMainMenu() {
        Espresso.onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(1500);
    }

}