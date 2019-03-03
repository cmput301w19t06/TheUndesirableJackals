package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.User.User;

public class Message extends Communication {
    private String text;
    private Boolean seen;

    public Message(User sender, User receiver, String text) {
        //super(sender, receiver);
        this.text = text;

        // now the object will add itself to the messages of the receiver
        receiver.addMessage(this);
    }

    public String getText() {
        return text;
    }
}
