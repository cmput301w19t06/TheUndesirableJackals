package com.cmput301.w19t06.theundesirablejackals;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookRequestHandlerTest {
    private User borrower;
    private User owner;
    private BookRequest bookRequest;
    private Book book;

    @Before
    public void setup() {
        borrower = new User("user1", "password1",
                    "user1@gmail.com", "7803123332");
        owner = new User("user2", "password2",
                    "user1@gmail.com", "7802231122");

        book = new Book("The Undesirable", "Jackal The Unknown",
                "99452212-0", owner);

        bookRequest = new BookRequest(borrower, owner, book);

    }

    @Test
    public void constructor_isCorrect() {

        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.PENDING);
        assertTrue(owner.getLendRequests().contains(bookRequest));
        assertTrue(borrower.getBorrowRequests().contains(bookRequest));

    }

    @Test
    public void denyRequest_isCorrect() {
        bookRequest.denyRequest();

        // check for request status change
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.DENIED);

        // check if status changed in owners side
    }

    @Test
    public void acceptRequest_isCorrect() {

    }

    @Test
    public void handOffBook_isCorrect() {
    }

    @Test
    public void receiveBookAsOwner_isCorrect() {
    }

    @Test
    public void returnBook_isCorrect() {
    }

    @Test
    public void recieveBookAsBorrower_isCorrect() {

    }
}