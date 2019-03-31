package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.activities.AlternateSignInActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class LogInTest {
    @Rule
    public ActivityTestRule<AlternateSignInActivity> testRule =
            new ActivityTestRule<AlternateSignInActivity>(AlternateSignInActivity.class);

    @Test
    public void logIn_isCorrect() {
        // input text in the EditTexts
        Espresso.onView(withId(R.id.editTextAlternateSignInEmail)).perform(typeText("rdrgues@hotmail.com"));
        Espresso.onView(withId(R.id.editTextAlternateSignInPassword)).perform(typeText("password"));

        // click the log in button
        Espresso.onView(withId(R.id.buttonAlternateSignIn)).perform(click());

        // check intent to "MainHomeViewActivity" after loging in
        Intents.init();
        intended(hasComponent(MainHomeViewActivity.class.getName()));

    }
}