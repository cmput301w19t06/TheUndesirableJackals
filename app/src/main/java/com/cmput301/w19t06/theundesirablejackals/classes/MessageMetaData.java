package com.cmput301.w19t06.theundesirablejackals.classes;

import java.util.ArrayList;

public class MessageMetaData {
    private ArrayList<Messaging> messagings;
    private String username;
    private Integer unseen;

    public MessageMetaData(){
        messagings = new ArrayList<Messaging>();
    }

    public ArrayList<Messaging> getMessagings() {
        return messagings;
    }

    public Integer getUnseen() {
        return unseen;
    }

    public String getUsername() {
        return username;
    }

    public void setMessagings(ArrayList<Messaging> messagings) {
        this.messagings = messagings;
    }

    public void setUnseen(Integer unseen) {
        this.unseen = unseen;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addUnseen(){
        unseen++;
    }

    @Override
    public String toString() {
        return "Messages:{" + messagings.toString() + "} " +
                "Message partner : " + username +
                "Number unseen : " + unseen.toString();
    }
}
