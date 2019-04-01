package com.cmput301.w19t06.theundesirablejackals;

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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
public class LogInLogOutTest {

    @Rule
    public ActivityTestRule<AlternateSignInActivity> testRule =
            new ActivityTestRule<AlternateSignInActivity>(AlternateSignInActivity.class);

    @Test
    public void logIn() {
        // input text in the EditTexts
        Espresso.onView(withId(R.id.editTextAlternateSignInEmail)).perform(typeText("rdrgues@hotmail.com"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.editTextAlternateSignInPassword)).perform(typeText("password"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(1500);
        // click the log in button
        Espresso.onView(withId(R.id.buttonAlternateSignIn)).perform(click());

        SystemClock.sleep(1500);
        openMainMenu();
        SystemClock.sleep(1500);

        // open profile
        Espresso.onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuProfile));

        SystemClock.sleep(1500);
        Espresso.pressBack();
        SystemClock.sleep(1500);

        // log out
        openMainMenu();
        Espresso.onView(withId(R.id.navigationViewMainHomeViewActivity))
                .perform(NavigationViewActions.navigateTo(R.id.itemMenuLogout));
        SystemClock.sleep(1500);

    }

    private void openMainMenu() {
        Espresso.onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(1500);
    }

}