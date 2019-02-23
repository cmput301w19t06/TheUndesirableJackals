package com.cmput301.w19t06.theundesirablejackals;

/**
 * Implements proper authentication process for users
 */
public class Authentication {
    /*
    Basic authentication process that will be replace in project part 4
    */
    private String userName;
    private String password; // does no have a get method i.e. cannot be accessed outside this class

    public Authentication(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * check if the entered password is the right match for the username
     * @param input This is the entered password
     * @return Boolean that which is true iff the entered password is right and false otherwise
     */
    public Boolean authenticate(String input) {
        // Returns true if user's input is the same as password
        //This is a quick change in authentication.java

        return password.equals(input); //Strings are not primitive "int's", don't use ==
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Sets a new username for the user
     * @param newUserName The new user name that the user wants to use
     */
    public void setUsername(String newUserName) {
        // Changes user name 
        userName = newUserName;
    }

    /**
     * Sets a new password for the user
     * @param newPassword This is the new password that the user wants to uze
     */
    public void setPassword(String newPassword) {
        // Assumption: Calls this method only if the user has been authenticated
        password = newPassword; 
    }
}
