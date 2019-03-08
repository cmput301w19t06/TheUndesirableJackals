package com.cmput301.w19t06.theundesirablejackals.Book;

import com.cmput301.w19t06.theundesirablejackals.Classes.Geolocation;
//import com.cmput301.w19t06.theundesirablejackals.Scanner;
import com.cmput301.w19t06.theundesirablejackals.User.User;

public class BookRequest {
    private Book bookRequested;
    private BookRequestStatus currentStatus;
    private User borrower;
    private Geolocation pickuplocation;
//    private Scanner scanner;

    public BookRequest() {

    }

    public BookRequest(User borrower, Book bookRequested) {
        this.bookRequested = bookRequested;
        this.currentStatus = BookRequestStatus.PENDING;
        this.borrower = borrower;

        // set the status of the book requested to "requested"
        bookRequested.setStatus(BookStatus.REQUESTED);

        // object adds itself into "lendRequests" of book owner
        bookRequested.getOwner().addBorrowRequest(this);

        // object adds itself into "borrowRequests" of sender
        borrower.addLendRequest(this);

    }

    public void denyRequest() {
        // TODO: Only Owner should deny request. NO ONE ELSE!
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.AVAILABLE);
        currentStatus = BookRequestStatus.DENIED;
    }

    public void acceptRequest() {
        // TODO: Only Owner can accept request. NO ONE ELSE!
        bookRequested.setStatus(BookStatus.ACCEPTED);
        currentStatus = BookRequestStatus.ACCEPTED;
    }

    public void handOffBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = BookRequestStatus.HANDED_OFF;
    }

    public void receiveBookAsOwner() {
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.AVAILABLE);
        currentStatus = BookRequestStatus.RECEIVED_OWNER;

        // then posibly delete this instance from the main
    }

    public void returnBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = BookRequestStatus.RETURNING;
    }

    public void recieveBookAsBorrower() {
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.BORROWED);
        currentStatus = BookRequestStatus.RECEIVED_BORROWER;
    }

    public Book getBookRequested() {
        return bookRequested;
    }

    public BookRequestStatus getCurrentStatus() {
        return currentStatus;
    }

}
