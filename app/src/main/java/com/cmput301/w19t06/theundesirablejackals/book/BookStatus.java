package com.cmput301.w19t06.theundesirablejackals.book;

/**
 * This enum is used to keep a finite amount of book status in check. These are the only possible
 * statuses that a book can have.
 * @author Art Limbaga
 * @see Book
 */
public enum BookStatus {
    AVAILABLE,  // book is available for all users to be borrowed
    REQUESTED,  // book is requested by a user
    ACCEPTED,   // book request has been accepted by the owner
    BORROWED    // book was handed off to the borrower

}
