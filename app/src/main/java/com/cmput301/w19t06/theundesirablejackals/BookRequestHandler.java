package com.cmput301.w19t06.theundesirablejackals;

public class BookRequestHandler /*extends Communication*/ {
    private Book bookRequested;
    private RequestStatus currentStatus;

    public enum RequestStatus{
        PENDING,            // book has been requested and waiting for owner to take action
        DENIED,             // book requested by the borrower is denied by the owner
        ACCEPTED,           // book request is accepted by book owner
        HANDED_OFF,         // book has been scanned for hand off by the owner
        RECEIVED_BORROWER,  // book has been scanned and received by the borrower
        RETURNING,          // book has been scanned for return by the borrower
        RECEIVED_OWNER      // returned book has been received by the owner
    }


    public BookRequestHandler(User sender, User receiver, Book bookRequested) {
        //super(sender, receiver);
        this.bookRequested = bookRequested;
        this.currentStatus = RequestStatus.PENDING;

        // set the status of the book requested to "requested"
        bookRequested.setStatus(Book.BookStatus.REQUESTED);

        // object adds itself into "lendRequests" of sender
        sender.addLendRequest(this);

        // object adds itself into "borrowRequests" of receiver
        receiver.addBorrowRequest(this);
    }

    public void denyRequest() {
        // TODO: Only Owner should deny request. NO ONE ELSE!
        // set status of the book back to "available"
        bookRequested.setStatus(Book.BookStatus.AVAILABLE);
        currentStatus = RequestStatus.DENIED;
    }

    public void acceptRequest() {
        // TODO: Only Owner can accept request. NO ONE ELSE!
        bookRequested.setStatus(Book.BookStatus.ACCEPTED);
        currentStatus = RequestStatus.ACCEPTED;
    }

    public void handOffBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = RequestStatus.HANDED_OFF;
    }

    public void receiveBookAsOwner() {
        // set status of the book back to "available"
        bookRequested.setStatus(Book.BookStatus.AVAILABLE);
        currentStatus = RequestStatus.RECEIVED_OWNER;

        // then posibly delete this instance from the main
    }

    public void returnBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = RequestStatus.RETURNING;
    }

    public void recieveBookAsBorrower() {
        // set status of the book back to "available"
        bookRequested.setStatus(Book.BookStatus.BORROWED);
        currentStatus = RequestStatus.RECEIVED_BORROWER;
    }
}
