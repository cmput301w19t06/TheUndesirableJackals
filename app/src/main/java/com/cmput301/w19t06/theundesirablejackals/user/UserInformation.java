package com.cmput301.w19t06.theundesirablejackals.user;

import java.io.Serializable;

/**
 * This class contains user information including contact information (email and phone) and username
 * @author  Art Limbaga
 * @see User
 */
public class UserInformation implements Serializable {

    private String userName = new String();
    private String userPhoto = new String();
    private String email = new String();
    private String phoneNumber = new String();


    /**
     * No Parameter constructor REQUIRED for firebase
     * DO NOT USE
     * @param u
     */
    public UserInformation() { }

    /**
     *
     * @param userName user name of the user
     * @param email email of the user
     * @param phoneNumber phone number of the user
     */
    public UserInformation(String userName, String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    /**
     *
     * @return the user name of the user
     */
    public String getUserName(){return userName;}

    /**
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param newEmail new email that will replace the old email
     */
    public void setEmail(String newEmail) {
        // May add ways to check it is a valid email format e.g. abc@abc.com
        email = newEmail;
    }

    /**
     *
     * @param newPhoneNumber phone number that will replace the old phone number
     */
    public void setPhoneNumber(String newPhoneNumber) {
        phoneNumber = newPhoneNumber;
    }

    /**
     * Required setter for Firebase
     * DO NOT USE
     * @param userName  the username that UserInfo username will be changed to
     */
    public void setUserName(String userName){ this.userName = userName;}

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public String toString() {
        return "{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
