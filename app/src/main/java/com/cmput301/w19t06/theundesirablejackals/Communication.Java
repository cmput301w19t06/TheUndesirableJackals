package com.cmput301.w19t06.theundesirablejackals;
import java.util.Date;

public abstract class Communication {
    private User sender;
    private User receiver;
    private Date date;
    private Boolean seen;

    public Communication(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        date = new Date(); // time and date
        seen = false;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setSeen() {
        seen = true;
    }
}
