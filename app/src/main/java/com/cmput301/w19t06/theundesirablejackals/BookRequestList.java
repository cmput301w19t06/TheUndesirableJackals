package com.cmput301.w19t06.theundesirablejackals;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookRequestList {
    private ArrayList<BookRequest> bookRequests;

    public BookRequestList() {
        bookRequests = new ArrayList<BookRequest>();
    }

    public void addRequest(BookRequest newBookRequest) {
        bookRequests.add(newBookRequest);
    }

    public void deleteRequest(BookRequest bookRequest) {
        bookRequests.remove(bookRequests);
    }

    public ArrayList<BookRequest> getBookRequests() {return bookRequests;}

    public ArrayList<BookRequest> getBookRequests(BookRequestStatus status) {
        //TODO: get book requests of certain status
        return bookRequests;
    }

    public ArrayList<BookRequest> getBookRequests(User user) {
        //TODO: get book requests for a certain user
        return bookRequests;
    }

    public boolean contains(BookRequest bookRequest) {
        return bookRequests.contains(bookRequest);
    }

}
