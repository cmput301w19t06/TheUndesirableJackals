package com.cmput301.w19t06.theundesirablejackals.User;

import java.io.Serializable;

public class UserInformation implements Serializable {

    private String userName;
    private String email;
    private String phoneNumber;

    public UserInformation() {

    }

    public UserInformation(String userName, String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public String getUserName(){return userName;}

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

    @Override
    public String toString() {
        return "UserInformation{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
