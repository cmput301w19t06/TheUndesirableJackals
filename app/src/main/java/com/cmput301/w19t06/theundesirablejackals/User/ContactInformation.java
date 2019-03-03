package com.cmput301.w19t06.theundesirablejackals.User;

public class ContactInformation {
    /* 
    May be remove if we can access this info through google account after
    we authenticate via Firebase
    */ 
    private String email;
    private String phoneNumber;

    public ContactInformation(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String newEmail) {
        // May add ways to check it is a valid email format e.g. abc@abc.com
        email = newEmail;
    }

    public static boolean isUniqueEmail(String email) {
        // TODO: will need to be implemented in the future
        return true;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        phoneNumber = newPhoneNumber;
    }
}
