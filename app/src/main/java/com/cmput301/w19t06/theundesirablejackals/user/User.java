package com.cmput301.w19t06.theundesirablejackals.user;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookGenres;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private UserInformation userinfo;
    private BookList ownedBooks;
    private BookList borrowedBooks;
    private BookList favouriteBooks;
    private Geolocation pickUpLocation;

    // user's requests to borrow books
    private BookRequestList lendRequests;

    // requests to borrow user's books
    private BookRequestList borrowRequests;

    //private UserNotificationList notifications;

    // topics of interest
    private ArrayList<BookGenres> genreOfInterests;

    private UserList friends;

    public User() {}

    public User(String userName, String email, String phoneNumber) {
        /* Lists containing books, requests and messages are set empty when 
        creating the User instance */
        userinfo = new UserInformation(userName, email, phoneNumber);
        ownedBooks = new BookList();
        borrowedBooks = new BookList();
        favouriteBooks = new BookList();
        lendRequests = new BookRequestList();
        borrowRequests = new BookRequestList();
        //notifications = new UserNotificationList();
        genreOfInterests = new ArrayList<BookGenres>();

        // pick up location as default at the U of A
        pickUpLocation = new Geolocation(53.5232, 113.5263);
    }

    public UserInformation getUserinfo(){
        return userinfo;
    }

    public BookList getOwnedBooks() {
        return ownedBooks;
    }

    public BookList getBorrowedBooks() {
        return borrowedBooks;
    }

    public BookRequestList getLendRequests() {
        return lendRequests;
    }

    public BookRequestList getBorrowRequests() {
        return borrowRequests;
    }

    public Geolocation getPickUpLocation() {
        return pickUpLocation;
    }


//    public UserNotificationList getNotifications() {
//        return notifications;
//    }
//
//    public void setNotifications(UserNotificationList notifications) {
//        this.notifications = notifications;
//    }

    public ArrayList<BookGenres> getGenreOfInterests() {
        return genreOfInterests;
    }

    public void setGenreOfInterests(ArrayList<BookGenres> interests) {
        this.genreOfInterests = interests;
    }

    public BookList getFavouriteBooks() {
        return favouriteBooks;
    }

    public void addFavouriteBooks(Book book) {
        favouriteBooks.addBook(book);
    }

    public void setFavouriteBooks(BookList favouriteBooks) {
        this.favouriteBooks = favouriteBooks;
    }

    public UserList getFriends() {
        return friends;
    }

    public void setFriends(UserList friends) {
        this.friends = friends;
    }

    public void addGenreOfInterest(BookGenres genre) {genreOfInterests.add(genre);}

    public void addFriend(User user) {}

    public void addLendRequest(BookRequest request) {
        lendRequests.addRequest(request);
    }

    public void addBorrowRequest(BookRequest request) {
        borrowRequests.addRequest(request);
    }

    @Override
    public String toString() {
        return "User{" +
                "userinfo=" + userinfo +
                ", ownedBooks=" + ownedBooks +
                ", borrowedBooks=" + borrowedBooks +
                ", favouriteBooks=" + favouriteBooks +
                ", lendRequests=" + lendRequests +
                ", borrowRequests=" + borrowRequests +
                ", genreOfInterests=" + genreOfInterests +
                ", friends=" + friends +
                '}';
    }
}
