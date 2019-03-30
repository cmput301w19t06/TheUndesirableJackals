package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInformationTest {
    UserInformation testInfoOne;
    UserInformation testInfoTwo;

    @Before
    public void setup() {
        testInfoOne = new UserInformation("Felipe", "sfrodrig@ualberta.ca",
                "7809155327");

        // testing empty constructor
        testInfoTwo = new UserInformation();
    }

    @Test
    public void getSetUserName_isCorrect() {
        testInfoTwo.setUserName("Art");

        assertEquals(testInfoTwo.getUserName(), "Art");
        assertEquals(testInfoOne.getUserName(), "Felipe");
    }

    @Test
    public void getSetEmail_isCorrect() {
        testInfoTwo.setEmail("art@ualberta.ca");

        assertEquals(testInfoTwo.getEmail(), "art@ualberta.ca");
        assertEquals(testInfoOne.getEmail(), "sfrodrig@ualberta.ca");
    }

    @Test
    public void getSetPhoneNumber_isCorrect() {
        testInfoTwo.setPhoneNumber("7777777777");

        assertEquals(testInfoTwo.getPhoneNumber(), "7777777777");
        assertEquals(testInfoOne.getPhoneNumber(), "7809155327");
    }
}