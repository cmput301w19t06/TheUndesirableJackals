package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotificationList;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotificationType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookRequestTest {
    private User borrower;
    private User owner;
    private BookRequest bookRequest;
    private Book book;

    @Before
    public void setup() {
        borrower = new User("makata", "makata@gmail.ca", "7803123332");
        owner = new User("poet", "poet@gmail.com", "7802231122");

        Book book = new Book("Noli me tangere", "José Rizal", "143322211");

        bookRequest = new BookRequest(borrower, book);

    }

    @Test
    public void constructor_isCorrect() {

        // check of request status has changed
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.PENDING);

        // check if the owner got the request
        assertTrue(owner.getLendRequests().contains(bookRequest));

        // check if the borrower registered the request
        assertTrue(borrower.getBorrowRequests().contains(bookRequest));

        // check if owner got a new notification of type NEW_BOOK_REQUEST
        UserNotificationList notifications = owner.getNotifications();
        assertTrue(owner.getNotifications().count() == 1);
        assertTrue(notifications.get(0).getType().equals(UserNotificationType.NEW_BOOK_REQUEST));

        // check if borrower book status changed to requested
        ArrayList<BookRequest> requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        Book book = requestedBooks.get(0).getBookRequested();
        assertEquals(book.getStatus(), BookStatus.REQUESTED);
    }

    @Test
    public void denyRequest_isCorrect() {
        bookRequest.denyRequest();

        // check if request status is DENIED
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.DENIED);

        // check if request status is DENIED
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.DENIED);

        // check if request status is DENIED
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.DENIED);

        // check if  borrower got a notification of type BOOK_REQUEST_UPDATE
        UserNotificationList notifications = borrower.getNotifications();
        assertTrue(borrower.getNotifications().count() == 1);
        assertTrue(notifications.get(0).getType().equals(UserNotificationType.BOOK_REQUEST_UPDATE));

        // check if book status has changed from requested to something else
        Book book = requestedBooks.get(0).getBookRequested();
        assertNotEquals(book.getStatus(), BookStatus.REQUESTED);

    }

    @Test
    public void acceptRequest_isCorrect() {
        bookRequest.acceptRequest();
        // check if request status is ACCEPTED
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.ACCEPTED);

        // check if request status in owners side is ACCEPTED
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.ACCEPTED);

        // check if request status is borrowers side is ACCEPTED
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.ACCEPTED);

        // check if borrower got a notification of type BOOK_REQUEST_UPDATE
        UserNotificationList notifications = borrower.getNotifications();
        assertTrue(borrower.getNotifications().count() == 1);
        assertTrue(notifications.get(0).getType().equals(UserNotificationType.BOOK_REQUEST_UPDATE));

        // check if book status is now ACCEPTED
        Book book = requestedBooks.get(0).getBookRequested();
        assertEquals(book.getStatus(), BookStatus.ACCEPTED);
    }

    @Test
    public void handOffBook_isCorrect() {
        bookRequest.acceptRequest();

        // check if request status is HANDED_OFF
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.HANDED_OFF);

        // check if request status in owners side is HANDED_OFF
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.HANDED_OFF);

        // check if request status in borrowers side is HANDED_OFF
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.HANDED_OFF);

    }

    @Test
    public void receiveBookAsBorrower_isCorrect() {
        bookRequest.recieveBookAsBorrower();

        // check if request status is RECEIVED_BORROWER
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.RECEIVED_BORROWER);

        // check if request status in owners side is RECEIVED_BORROWER
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RECEIVED_BORROWER);

        // check if request status in borrowers side is RECEIVED_BORROWER
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RECEIVED_BORROWER);

        // check of book status has changed to BORROWED
        Book book = requestedBooks.get(0).getBookRequested();
        assertEquals(book.getStatus(), BookStatus.BORROWED);
    }

    @Test
    public void returnBook_isCorrect() {
        bookRequest.returnBook();

        // check if request status is RETURNING
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.RETURNING);

        // check if request status in owners side is RETURNING
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RETURNING);

        // check if request status in borrowers side is RETURNING
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RETURNING);

        // check if owner got a notification for borrower wanting to return the book
        UserNotificationList notifications = owner.getNotifications();
        assertTrue(owner.getNotifications().count() == 1);
        assertTrue(notifications.get(0).getType().equals(UserNotificationType.BOOK_REQUEST_UPDATE));
    }


    @Test
    public void receiveBookAsOwner_isCorrect() {
        bookRequest.receiveBookAsOwner();

        // check if request status is RECEIVED_OWNER
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.RECEIVED_OWNER);

        // check if request status in owners side is RECEIVED_OWNER
        ArrayList<BookRequest> requestedBooks = owner.getLendRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RECEIVED_OWNER);

        // check if request status in borrowers side is RECEIVED_OWNER
        requestedBooks = borrower.getBorrowRequests().getBookRequests();
        assertTrue(requestedBooks.size() == 1);
        assertEquals(requestedBooks.get(0).getCurrentStatus(), BookRequestStatus.RECEIVED_OWNER);

        // check of book status has changed to BORROWED
        Book book = requestedBooks.get(0).getBookRequested();
        assertEquals(book.getStatus(), BookStatus.AVAILABLE);

    }



}