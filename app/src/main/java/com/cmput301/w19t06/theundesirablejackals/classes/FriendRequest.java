package com.cmput301.w19t06.theundesirablejackals.classes;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.io.Serializable;

/**
 * Class for friend request handling
 * @author Art Limbaga
 */
public class FriendRequest implements Serializable {
    private UserInformation requestSender;
    private UserInformation requestReceiver;

    private String requestKey;

    public FriendRequest(){}

    /**
     * Create a friend request without the database keys
     * @param requestSender
     * @param requestReceiver
     */
    public FriendRequest(UserInformation requestSender, UserInformation requestReceiver) {
        this.requestReceiver = requestReceiver;
        this.requestSender = requestSender;
        this.requestKey = new String();
    }

    /**
     * Create a friend request with the database keys
     * @param requestSender
     * @param requestReceiver
     * @param requestKey
     */
    public FriendRequest(UserInformation requestSender, UserInformation requestReceiver, String requestKey) {
        this.requestReceiver = requestReceiver;
        this.requestSender = requestSender;
        this.requestKey = requestKey;
    }


    public UserInformation getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(UserInformation requestSender) {
        this.requestSender = requestSender;
    }

    public UserInformation getRequestReceiver() {
        return requestReceiver;
    }

    public void setRequestReceiver(UserInformation requestReceiver) {
        this.requestReceiver = requestReceiver;
    }


    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
}
