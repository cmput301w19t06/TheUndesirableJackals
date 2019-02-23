package com.cmput301.w19t06.theundesirablejackals;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthenticationTest {
    private Authentication authentication;
    @Before
    public void setup() {
        authentication = new Authentication("Jackal", "Undesirable");
    }

    @Test
    public void authenticate_isCorrect() {
        assertFalse(authentication.authenticate("wrongpassword"));
        assertTrue(authentication.authenticate("Undesirable"));
    }

    @Test
    public void setUsername_isCorrect() {
        authentication.setUsername("NeverLucky");
        assertEquals(authentication.getUserName(), "NeverLucky");

    }

    @Test
    public void setPassword_isCorrect() {
        authentication.setPassword("rightpassword");
        assertTrue(authentication.authenticate("rightpassword"));
    }

}