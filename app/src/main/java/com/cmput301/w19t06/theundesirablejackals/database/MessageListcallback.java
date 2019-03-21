package com.cmput301.w19t06.theundesirablejackals.database;

import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;

import java.util.ArrayList;

public interface MessageListcallback {
    void onCallback(ArrayList<Messaging> messagingArrayList);
}
