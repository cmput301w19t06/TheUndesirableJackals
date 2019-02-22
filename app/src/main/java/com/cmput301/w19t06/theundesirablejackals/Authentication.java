package com.cmput301.w19t06.theundesirablejackals;

public class Authentication {
    /*
    Basic authentication process that will be replace in project 4
    */
    private String userName;
    private String password; // does no have a get method i.e. cannot be 
                             // accessed outside this class

    public Authentication(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Boolean authenticate(String input) {
        // Returns true if user's input is the same as password


        return password.equals(input); //Strings are not primitive "int's", don't use ==
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String newUserName) {
        // Changes user name 
        userName = newUserName;
    }

    public void setPassword(String newPassword) {
        // Assumption: Calls this method only if the user has been authenticated
        password = newPassword; 
    }
}
