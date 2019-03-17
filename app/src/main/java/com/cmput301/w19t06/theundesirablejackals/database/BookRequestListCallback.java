package com.cmput301.w19t06.theundesirablejackals.database;


import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;

public interface BookRequestListCallback {
    void onCallback(BookRequestList bookRequestList);
}
