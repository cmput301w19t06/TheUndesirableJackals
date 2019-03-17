package com.cmput301.w19t06.theundesirablejackals.database;

import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;

public interface BookRequestCallback {
    void onCallback(BookRequest bookRequest);
}
