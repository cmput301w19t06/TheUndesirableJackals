package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookGenres;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    private User owner;
    private User borrower;
    private BookRequest bookRequest;
    private Book bookRequested;


    @Before
    public  void setup() {
        owner = new User("felipe", "email@hotmail.com", "333-333-3333");
        borrower = new User("oriwa", "emailu@hotmail.com", "333-333-4444");

        bookRequested = new Book("Noli me tangere", "José Rizal", "143322211");

        bookRequest = new BookRequest(borrower, bookRequested);

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