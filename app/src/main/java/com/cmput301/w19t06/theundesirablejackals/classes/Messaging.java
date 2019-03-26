package com.cmput301.w19t06.theundesirablejackals.classes;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Message class used by our app to keep track of the messages between users
 */
@SuppressWarnings("unused")
public class Messaging implements Serializable {
    private String from;
    private String to;
    private String receiverKey;
    private String senderKey;
    private String message;
    private Boolean seen = false;

    /**
     * Default constructor, required for Firebase
     */
    public Messaging(){}

    public Messaging(@NonNull Messaging messaging){
        this.from = messaging.getFrom();
        this.to = messaging.getTo();
        this.receiverKey = messaging.getReceiverKey();
        this.senderKey = messaging.getSenderKey();
        this.message = messaging.getMessage();
    }

    /**
     * Getter for the from field of this message
     * @return A string containing the username of the sender
     */
    public String getFrom() {
        return from;
    }


    /**
     * Getter for the message field of this message
     * @return A string containing the message itself
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for the Recieverkey (for use by DatabaseHerlper and Firebase)
     * @return
     */
    public String getReceiverKey() {
        return receiverKey;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "from" + from + "to" + to +
        "seen" + seen + "receiverKey" + receiverKey +
        "senderKey" + senderKey + "message" + message +
        "seen" + seen.toString();

    }
}
