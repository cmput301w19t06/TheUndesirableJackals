package com.cmput301.w19t06.theundesirablejackals;

/**
 * Add a book and API testing
 * UI test that logs in and selects the option to add a book
 * It will fill the ISBN section of the book and wait for a response from Google Books API
 * It will check the retrieved data of author and categories and compare it with the parameters
 * we have
 * @Version March 31, 2019
 */

import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class booksAPITest {

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
    public void booksAPI_isCorrect() {
        // switch to "my books" tab if not there already
        Espresso.onView(withId(R.id.tabLayoutMainHomeViewActivityFragmentTabs)).
                perform(selectTabAtPosition(0));

        SystemClock.sleep(3000);

        // clicks on create a book
        Espresso.onView(withId(R.id.floatingActionButtonAddNewBook)).perform(click());
        SystemClock.sleep(1500);

        // the correct book description
        String isbn = "9780307743657";
        String author = "Stephen King";
        String categories = "Fiction";

        // fill the ISBN
        Espresso.onView(withId(R.id.editTextAddBookBookISBN)).perform(typeText(isbn));
        Espresso.closeSoftKeyboard();

        // move the cursor away
        Espresso.onView(withId(R.id.editTextAddBookBookTitle)).perform(typeText("."));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(3000);

        // checks the description from Google Books API is correct
        Espresso.onView(withId(R.id.editTextAddBookBookAuthor)).check(matches(withText(containsString(author))));
        Espresso.onView(withId(R.id.editTextAddBookBookCategories)).check(matches(withText(containsString(categories))));

        SystemClock.sleep(1500);

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