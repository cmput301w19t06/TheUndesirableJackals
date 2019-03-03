package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.User.ContactInformation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactInformationTest {
    private ContactInformation contactInformation;
    @Before
    public void setup() {
        contactInformation = new ContactInformation("nanithefaq@gmail.com", "7809990999");

    }
    @Test
    public void setEmail_isCorrect() {
        String emailBefore = contactInformation.getEmail();
        contactInformation.setEmail("nani@gmail.com");
        assertNotEquals(contactInformation.getEmail(), emailBefore);
    }

    @Test
    public void isUniqueEmail_isCorrect() {
        // TO BE IMPLEMENTED Test will be unsatisfactory

        // creation of a new contact will render the used email not unique
        assertFalse(contactInformation.isUniqueEmail("nani@gmail.com"));

    }

    @Test
    public void setPhoneNumber_isCorrect() {
        String phoneBefore = contactInformation.getPhoneNumber();
        contactInformation.setPhoneNumber("78012333321");
        assertNotEquals(phoneBefore, contactInformation.getPhoneNumber());

    }
}