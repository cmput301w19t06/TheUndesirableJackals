package com.cmput301.w19t06.theundesirablejackals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    private User owner;
    private User borrower;
    private BookRequest bookRequest;
    private Book bookRequested;
    private Message message;


    @Before
    public  void setup() {
        owner = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");
        borrower = new User("oriwa", "passu",
                "emailu@hotmail.com", "333-333-4444");

        bookRequested = new Book("The Undesirable", "Jackal The Unknown",
                "99452212-0", owner);

        bookRequest = new BookRequest(borrower, owner, bookRequested);

        message = new Message(borrower, owner, "I want to borrower your book");
    }

    @Test
    public void addLendRequest_isCorrect() {
        // first get the "BookRequestList" object
        BookRequestList object = owner.getLendRequests();

        // then get the list of books
        ArrayList<BookRequest> listOfBooks = object.getBookRequests();

        // now check if book is in there
        assertTrue(listOfBooks.contains(bookRequest));
    }

    @Test
    public void addBorrowRequest_isCorrect() {
        assertTrue(borrower.getBorrowRequests().contains(bookRequest));
    }

    @Test
    public void addMessage_isCorrect() {

        // check if owner got the message
        assertTrue(owner.getMessages().contains(message));
    }

    @Test
    public void addFriend_isCorrect() {
        borrower.addFriend(owner);
        assertTrue(borrower.getFriends().contains(owner));
    }

    @Test
    public void addGenreOfInterests_isCorrect() {
        borrower.addGenreOfInterest(BookGenres.AUTOBIOGRAPHY);
        assertTrue(borrower.getGenreOfInterests().contains(BookGenres.AUTOBIOGRAPHY));
    }
}