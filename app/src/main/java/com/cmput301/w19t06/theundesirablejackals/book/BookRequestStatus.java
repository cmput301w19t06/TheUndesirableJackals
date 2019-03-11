package com.cmput301.w19t06.theundesirablejackals.book;

/**
 * These are the only possible statuses that a book request can have
 * @author Art Limbaga
 * @see BookRequest
 */
public enum BookRequestStatus {
    PENDING,            // book has been requested and waiting for owner to take action
    DENIED,             // book requested by the borrower is denied by the owner
    ACCEPTED,           // book request is accepted by book owner
    HANDED_OFF,        // book has been scanned for hand off by the owner
    RECEIVED_BORROWER,  // book has been scanned and received by the borrower
    RETURNING,          // book has been scanned for return by the borrower
    RECEIVED_OWNER      // returned book has been received by the owner
}
