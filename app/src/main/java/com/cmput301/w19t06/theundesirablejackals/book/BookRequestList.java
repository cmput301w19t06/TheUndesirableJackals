package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.util.ArrayList;

/**
 * This class is responsible for managing a list of BookRequests through searching and
 * filtering.
 * @author Art Limbaga
 * @see BookRequest
 */
public class BookRequestList {
    private ArrayList<BookRequest> bookRequests;

    /**
     * Creates an empty BookRequestList. Usually called when a user is newly created
     */
    public BookRequestList() {
        bookRequests = new ArrayList<BookRequest>();
    }

    /**
     * @param newBookRequest BookRequest object to be added to the list
     */
    public void addRequest(BookRequest newBookRequest) {
        bookRequests.add(newBookRequest);
    }

    /**
     *
     * @param bookRequest BookRequest object to be deleted in the request list
     */
    public void deleteRequest(BookRequest bookRequest) {
        bookRequests.remove(bookRequests);
    }


    /**
     *
     * @return an ArrayList of  all the BookRequest object in the list
     */
    public ArrayList<BookRequest> getBookRequests() {return bookRequests;}


    /**
     * Get all book request based on BoookRequestStatus
     * @param status status of the book that the contains
     * @return an ArrayList of BookRequests which matches the status
     */
    public ArrayList<BookRequest> getBookRequests(BookRequestStatus status) {
        //TODO: get book requests of certain status
        return bookRequests;
    }


    /**
     * Get all the book request from this list that is tied to the user
     * @param user can either be the owner or the borrower of the book request
     * @return an ArrayList of BookRequest that is tied to the user
     */
    public ArrayList<BookRequest> getBookRequests(User user) {
        //TODO: get book requests for a certain user
        return bookRequests;
    }


    /**
     * Get all the BookRequest that invlolves the book
     * @param book the book that is requested
     * @return an ArrayList of BookRequest which contains all the request regarding the book
     */
    public ArrayList<BookRequest> getBookRequests(Book book) {
        return bookRequests;
    }

    /**
     * @param bookRequest to be checked in the list of BookRequests
     * @return false if the book is not found in the list, otherwise true.
     */
    public boolean contains(BookRequest bookRequest) {
        return bookRequests.contains(bookRequest);
    }

    public int size(){return bookRequests.size();}

    public BookRequest get(Integer integer){return bookRequests.get(integer);}

}
