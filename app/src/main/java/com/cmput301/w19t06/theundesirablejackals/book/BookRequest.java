package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
//import com.cmput301.w19t06.theundesirablejackals.Scanner;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

/**
 * This class mainly handles the book exchange between two users. It tracks the current situation
 * of the book request through the use of BookStatus enum.
 * @author Art Limbaga
 * @author Felipe Rodriguez
 * @see Book
 * @see User
 * @see Geolocation
 */
public class BookRequest {
    private Book bookRequested;
    private BookRequestStatus currentStatus;
    private UserInformation borrower;
    private Geolocation pickuplocation;
//    private Scanner scanner;

    /**
     * @param borrower the borrower of the book
     * @param bookRequested book requested by the borrower
     */
    public BookRequest(UserInformation borrower, Book bookRequested) {
        this.bookRequested = bookRequested;
        this.currentStatus = BookRequestStatus.PENDING;
        this.borrower = borrower;

        // set the status of the book requested to "requested"
        bookRequested.setStatus(BookStatus.REQUESTED);

        // object adds itself into "lendRequests" of book owner
//        bookRequested.getOwner().addBorrowRequest(this);

        // object adds itself into "borrowRequests" of sender
//        borrower.addLendRequest(this);

    }

    /**
     * Owner of the book has denied the borrowers request. This will also be called to deny all
     * other book requests when a book request has been accepted for a user
     */
    public void denyRequest() {
        // TODO: Only Owner should deny request. NO ONE ELSE!
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.AVAILABLE);
        currentStatus = BookRequestStatus.DENIED;
    }

    /**
     * Owner has accepted the book request, which denies all other book requests
     */
    public void acceptRequest() {
        // TODO: Only Owner can accept request. NO ONE ELSE!
        bookRequested.setStatus(BookStatus.ACCEPTED);
        currentStatus = BookRequestStatus.ACCEPTED;
    }

    public UserInformation getBorrower(){return borrower;}

    public void setBorrower(UserInformation userInformation){this.borrower = userInformation;}

    /**
     * Book has been handed off by the owner and yet to be received by the accepted borrower. This
     * is done when the owner scans  an accepted book request for hand off.
     */
    public void handOffBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = BookRequestStatus.HANDED_OFF;
    }

    /**
     * Book has been has been returned to the owner. This is done by scanning a book that has
     * a RETURNING book request status
     */
    public void receiveBookAsOwner() {
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.AVAILABLE);
        currentStatus = BookRequestStatus.RECEIVED_OWNER;

        // then posibly delete this instance from the main
    }

    /**
     * A borrowers action which returns the book to the owner and changes the book request status
     * to RETURNING. This is done when the borrower scans the book for return
     */
    public void returnBook() {
        // owner scans the book to note that it has been handed off to borrower
        // TODO: need to actually implement scanning in the future
        currentStatus = BookRequestStatus.RETURNING;
    }

    /**
     * Book has been has been received by the borrower which will change the book request status
     * to Borrowed. This is done when a borrower scans a book with the HANDED_OFF book request status
     */
    public void recieveBookAsBorrower() {
        // set status of the book back to "available"
        bookRequested.setStatus(BookStatus.BORROWED);
        currentStatus = BookRequestStatus.RECEIVED_BORROWER;
    }

    /**
     *
     * @return the book requested in this book request
     */
    public Book getBookRequested() {
        return bookRequested;
    }


    /**
     *
     * @return returns the current status of the book request
     */
    public BookRequestStatus getCurrentStatus() {
        return currentStatus;
    }

}
