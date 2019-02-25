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


    /**
     * Get all book request based on BoookRequestStatus
     * @param status status of the book that the contains
     * @return
     */
    public ArrayList<BookRequest> getBookRequests(BookRequestStatus status) {
        //TODO: get book requests of certain status
        return bookRequests;
    }


    /**
     * Get all book request for a certain user
     * @param user
     * @return
     */
    public ArrayList<BookRequest> getBookRequests(User user) {
        //TODO: get book requests for a certain user
        return bookRequests;
    }


    /**
     * Get book requests of a user given a book
     * @param usr
     * @param book
     * @return
     */
    public ArrayList<BookRequest> getBookRequests(User user, Book book) {
        return bookRequests;
    }

    /**
     * Get book requests for a certain book
     * @param book
     * @return
     */
    public ArrayList<BookRequest> getBookRequests(Book book) {
        return bookRequests;
    }

    public boolean contains(BookRequest bookRequest) {
        return bookRequests.contains(bookRequest);
    }

}
