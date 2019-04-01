package com.cmput301.w19t06.theundesirablejackals.book;

import java.io.Serializable;

/**
 * These are the only possible statuses that a book request can have
 * @author Art Limbaga
 * @see BookRequestStatus
 */
public enum BookRequestStatus implements Serializable {
    REQUESTED("REQUESTED"),     // book has been requested and waiting for owner to take action
    DENIED("DENIED"),           // book requested by the borrower is denied by the owner
    ACCEPTED("ACCEPTED"),       // book request is accepted by book owner
    HANDED_OFF("HANDED OFF"),   // book has been scanned for hand off by the owner
    BORROWED("BORROWED"),       // book has been scanned and received by the borrower
    RETURNING("RETURNING"),     // book has been scanned for return by the borrower
    RETURNED("RETURNED") ;      // returned book has been received by the owner

    private final String mStatusDescription;

    private BookRequestStatus(String StatusDescription) {
        this.mStatusDescription = StatusDescription;
    }

    public String getStatusDescription() {
        return mStatusDescription;
    }

}
