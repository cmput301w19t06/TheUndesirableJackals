package com.cmput301.w19t06.theundesirablejackals.database;

import com.cmput301.w19t06.theundesirablejackals.classes.FriendRequest;

import java.util.ArrayList;

public interface FriendRequestListCallback {
    void onCallback(ArrayList<FriendRequest> friendRequests);
}
