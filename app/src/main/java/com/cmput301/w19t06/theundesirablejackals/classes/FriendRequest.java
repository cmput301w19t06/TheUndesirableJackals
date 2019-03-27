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
    private boolean requestAccepted;
    private String requestSenderKey;
    private String requestReceiverKey;

    public FriendRequest(){}

    /**
     * Create a friend request without the database keys
     * @param requestSender
     * @param requestReceiver
     */
    public FriendRequest(UserInformation requestSender, UserInformation requestReceiver) {
        this.requestReceiver = requestReceiver;
        this.requestSender = requestSender;
        this.requestAccepted = false;
    }

    /**
     * Create a friend request with the database keys
     * @param requestSender
     * @param requestReceiver
     * @param requestSenderKey
     * @param requestReceiverKey
     */
    public FriendRequest(UserInformation requestSender, UserInformation requestReceiver, String requestSenderKey, String requestReceiverKey) {
        this.requestReceiver = requestReceiver;
        this.requestSender = requestSender;
        this.requestSenderKey = requestSenderKey;
        this.requestReceiverKey= requestReceiverKey;
        this.requestAccepted = false;
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

    public boolean isRequestAccpeted() {
        return requestAccepted;
    }

    public void setRequestAccpeted(boolean requestAccpeted) {
        this.requestAccepted = requestAccpeted;
    }

    public String getRequestSenderKey() {
        return requestSenderKey;
    }

    public void setRequestSenderKey(String requestSenderKey) {
        this.requestSenderKey = requestSenderKey;
    }

    public String getRequestReceiverKey() {
        return requestReceiverKey;
    }

    public void setRequestReceiverKey(String requestReceiverKey) {
        this.requestReceiverKey = requestReceiverKey;
    }
}
