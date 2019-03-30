package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotificationList;
import com.cmput301.w19t06.theundesirablejackals.user.UserNotificationType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/*
Contain tests of all nested classes in BookRequest:
    BookInformation
    BookRequestStatus
    UserInformation
    Geolocation
 */
public class BookRequestTest {
    private BookInformation bookRequested;
    private BookRequestStatus currentStatus;
    private UserInformation borrower;
    private Geolocation pickupLocation;
    private BookRequest bookRequest;

    @Before
    public void setup() {
        // create all parameters and objects needed to test BookRequest
        bookRequested = new BookInformation(BookStatus.AVAILABLE, "9780307743657", "Felipe");
        currentStatus = BookRequestStatus.REQUESTED;
        borrower = new UserInformation("Art", "art@mail.com", "7777777777");
        pickupLocation = new Geolocation(53.521331248, -113.521331248);

        // create the actual object that will be tested
        bookRequest = new BookRequest(borrower, bookRequested);

    }

    @Test
    public void getSetKeys_isCorrect() {
        String borrowerKey = "AJKUDIS";
        String lenderKey = "SDFDSLK";

        bookRequest.setBookRequestBorrowKey(borrowerKey);
        bookRequest.setBookRequestLendKey(lenderKey);

        assertEquals(bookRequest.getBookRequestBorrowKey(), borrowerKey);
        assertEquals(bookRequest.getBookRequestLendKey(), lenderKey);
    }

    @Test
    public void getSetPickupLocation() {
        bookRequest.setPickuplocation(pickupLocation);
        Geolocation checkLoc = bookRequest.getPickuplocation();

        assertEquals(checkLoc.getLatitude(), pickupLocation.getLatitude());
        assertEquals(checkLoc.getLongitude(), pickupLocation.getLongitude());
    }

    @Test
    public void getSetStatus_isCorrrect() {
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.REQUESTED);
        bookRequest.setCurrentStatus(BookRequestStatus.ACCEPTED);
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.ACCEPTED);
    }

    @Test
    public void getBookInfo_isCorrect() {
        BookInformation bookInfo = bookRequest.getBookRequested();

        assertEquals(bookInfo.getIsbn(), bookRequested.getIsbn());
    }

    @Test
    public void getBorrower() {
        UserInformation borrowerInfo = bookRequest.getBorrower();
        assertEquals(borrowerInfo.getEmail(), borrower.getEmail());
        assertEquals(borrowerInfo.getPhoneNumber(), borrower.getPhoneNumber());
    }

    @Test
    public void acceptRequest() {
        bookRequest.acceptRequest();

        // check the book requested has change his status
        BookInformation book = bookRequest.getBookRequested();
        assertEquals(book.getStatus(), BookStatus.ACCEPTED);

        // check the request has changed the status
        assertEquals(bookRequest.getCurrentStatus(), BookRequestStatus.ACCEPTED);
    }

}