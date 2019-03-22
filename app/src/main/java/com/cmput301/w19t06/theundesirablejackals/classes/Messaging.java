package com.cmput301.w19t06.theundesirablejackals.classes;

public class Messaging{
    private String from;
    private String to;
    private String receiverKey;
    private String senderKey;
    private String message;

    public Messaging(){}

    public Messaging(Messaging messaging){
        this.from = messaging.getFrom();
        this.to = messaging.getTo();
        this.receiverKey = messaging.getReceiverKey();
        this.senderKey = messaging.getSenderKey();
        this.message = messaging.getMessage();
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

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
}
