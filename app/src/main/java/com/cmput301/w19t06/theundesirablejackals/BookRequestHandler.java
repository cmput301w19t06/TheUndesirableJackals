package com.cmput301.w19t06.theundesirablejackals;

public class BookRequestHandler extends Communication {
    private Book bookRequested;
    private String requestStatus; // "pending", "denied" or "accepted"


    public BookRequestHandler(User sender, User receiver, Book bookRequested) {
        super(sender, receiver);
        this.bookRequested = bookRequested;
        requestStatus = "pending";

        // set the status of the book requested to "requested"
        bookRequested.setStatus("requested");

        // object adds itself into "lendRequests" of sender
        sender.addLendRequest(this);

        // object adds itself into "borrowRequests" of receiver
        receiver.addBorrowRequest(this);
    }

    public void denyRequest() {
        // set status of the book back to "available"
        bookRequested.setStatus("available");
        requestStatus = "denied";
    }

    public void acceptRequest() {
        bookRequested.setStatus("accepted");
        requestStatus = "accepted";
    }

    public void giveBook() {
        // owner gives book to the borrower
        bookRequested.setStatus("borrowed");
    }

    public void returnBook() {
        // set status of the book back to "available"
        bookRequested.setStatus("available");

        // then posibly delete this instance from the main 
    }

}
