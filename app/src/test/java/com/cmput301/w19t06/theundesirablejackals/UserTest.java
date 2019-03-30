package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User owner;
    private User borrower;


    @Before
    public  void setup() {
        owner = new User("Felipe", "sfrodrig@ualberta.ca", "7809155327");

        // testing the empty constructor
        borrower = new User();
    }

    @Test
    public void userInformation_isCorrect() {
        UserInformation checkUser = owner.getUserInfo();
        assertEquals(checkUser.getEmail(), "sfrodrig@ualberta.ca");
        assertEquals(checkUser.getUserName(), "Felipe");

        // adding the attributes of borrower with set methods
        UserInformation borrowerInfo = new UserInformation("Art", "art@ualberta.ca", "7777777777");
        borrower.setUserInfo(borrowerInfo);

        assertEquals(borrower.getUserInfo(), borrowerInfo);
    }

    @Test
    public void getSetGeolocation_isCorrect() {
        Geolocation defaultLocation = new Geolocation(53.5232, -113.5263);
        Geolocation newLocation = new Geolocation(35.44, -111.789);

        // check the default is set properly
        assertEquals(owner.getPickUpLocation().getLatitude(), defaultLocation.getLatitude());

        owner.setPickUpLocation(newLocation);
        assertEquals(owner.getPickUpLocation().getLongitude(), newLocation.getLongitude());
    }

    @Test
    public void ownedBooks_isCorrect() {
        Book bookOne = new Book("Don Quijote de la Mancha", "Miguel de Cervantes",
                "9788420412146");

        BookToInformationMap owned = owner.getOwnedBooks();
        owned.addBook(bookOne.getIsbn(), "ABC123");
        owner.setOwnedBooks(owned);

        BookToInformationMap checkingOwned = owner.getOwnedBooks();
        assertNotNull(checkingOwned.get("9788420412146"));
        assertNull(checkingOwned.get("9780321268457"));
    }

    @Test
    public void borrowedBooks_isCorrect() {
        Book bookTwo = new Book("Database Systems", "Michael Kifer", "9780321268457");

        BookToInformationMap borrowed = owner.getBorrowedBooks();
        borrowed.addBook(bookTwo.getIsbn(), "ASD145");
        owner.setBorrowedBooks(borrowed);

        BookToInformationMap checkingBorrowed = owner.getBorrowedBooks();
        assertNull(checkingBorrowed.get("9788420412146"));
        assertNotNull(checkingBorrowed.get("9780321268457"));
    }
}