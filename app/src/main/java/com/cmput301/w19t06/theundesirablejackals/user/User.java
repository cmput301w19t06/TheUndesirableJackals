package com.cmput301.w19t06.theundesirablejackals.user;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.book.BookGenres;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;

import java.util.ArrayList;

/**
 * This class handles the users interaction with the application including book request, owned
 * book collection, friends, notifications, and borrowed books.
 * @author Art Limbaga
 * @author Felipe Rodriguez
 * @see BookRequestList
 * @see UserNotification
 * @see Geolocation
 * @see BookToInformationMap
 * @see UserList
 */
public class User {
    private Geolocation pickUpLocation;
    private UserInformation userInfo;
    private BookToInformationMap ownedBooks;
    private BookToInformationMap borrowedBooks;



    public User() {
        userInfo = new UserInformation();
        ownedBooks = new BookToInformationMap();
        borrowedBooks = new BookToInformationMap();

        // pick up location as default at the U of A
        pickUpLocation = new Geolocation(53.5232, -113.5263);
    }

    /**
     * Instantiates a user with the following username, email, and phone number
     * @param userName username of the user
     * @param email email of the user
     * @param phoneNumber phone number of the user
     */
    public User(String userName, String email, String phoneNumber) {
        /* Lists containing books, requests and messages are set empty when 
        creating the User instance */
        userInfo = new UserInformation(userName, email, phoneNumber);
        ownedBooks = new BookToInformationMap();
        borrowedBooks = new BookToInformationMap();

        // pick up location as default at the U of A
        pickUpLocation = new Geolocation(53.5232, -113.5263);
    }


    /**
     *
     * @return UserInformation object containing the information about the user
     */
    public UserInformation getUserInfo(){
        return userInfo;
    }


    /**
     *
     * @return Books that is owned by the user
     */
    public BookToInformationMap getOwnedBooks() {
        return ownedBooks;
    }


    /**
     *
     * @return a BookToInformationMap of all the books borrowed  by the user
     */
    public BookToInformationMap getBorrowedBooks() {
        return borrowedBooks;
    }


    /**
     *
     * @return the default pickuplocation set by the user
     */
    public Geolocation getPickUpLocation() {
        return pickUpLocation;
    }


    /**
     * Required setter for Firebase
     * @param geolocation  the geolocation which will set the pickuplocation for User user
     */
    public void setPickUpLocation(Geolocation geolocation){this.pickUpLocation = geolocation;}


    /**
     * Required setter for Firebase
     * @param borrowedBooks
     */
    public void setBorrowedBooks(BookToInformationMap borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }


    /**
     * Required setter for Firebase
     * @param ownedBooks
     */
    public void setOwnedBooks(BookToInformationMap ownedBooks) {
        this.ownedBooks = ownedBooks;
    }


    /**
     * Add an owned book to user's owned book list, and set user as owner
     * @param book
     */
    public void addOwnedBook(Book book, String descriptionKey){
        this.ownedBooks.addBook(book.getIsbn(), descriptionKey);
    }


    /**
     * Required setter for Firebase
     * @param userInfo
     */
    public void setUserInfo(UserInformation userInfo) {
        this.userInfo = userInfo;
    }


    @Override
    public String toString() {
        return "User{" +
                "\nuserInfo=" + userInfo +
                ",\nownedBooks=" + ownedBooks +
                ",\nborrowedBooks=" + borrowedBooks +
                ",\npickUpLocation="+ pickUpLocation +
                '}';
    }
}
