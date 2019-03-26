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
    private BookToInformationMap favouriteBooks;


    private UserNotificationList notifications;

    // topics of interest
    private ArrayList<BookGenres> genreOfInterests;

    private UserList friends;


    public User() {
        userInfo = new UserInformation();
        ownedBooks = new BookToInformationMap();
        borrowedBooks = new BookToInformationMap();
        favouriteBooks = new BookToInformationMap();
        notifications = new UserNotificationList();
        genreOfInterests = new ArrayList<BookGenres>();
        friends = new UserList();

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
        favouriteBooks = new BookToInformationMap();
//        lendRequests = new BookRequestListCallback();
//        borrowRequests = new BookRequestListCallback();
        notifications = new UserNotificationList();
        genreOfInterests = new ArrayList<BookGenres>();
        friends = new UserList();

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
     * @return BookRequestListCallback of all the lend requests of the user
     */
//    public BookRequestListCallback getLendRequests() {
//        return lendRequests;
//    }

    /**
     *
     * @return BookRequestListCallback of all the barrow BookRequest of the user
     */
//    public BookRequestListCallback getBorrowRequests() {
//        return borrowRequests;
//    }

    /**
     *
     * @return the default pickuplocation set by the user
     */
    public Geolocation getPickUpLocation() {
        return pickUpLocation;
    }

    /**
     *
     * @return the list of notifications for the user
     */
    public UserNotificationList getNotifications() {
        return notifications;
    }

    /**
     * @param notifications list that will overwrite the current notification list of the user
     */
    public void setNotifications(UserNotificationList notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @return returns an ArrayList of all the genres that the user is interested in
     */
    public ArrayList<BookGenres> getGenreOfInterests() {
        return genreOfInterests;
    }

    /**
     *
     * @param interests sets the genre of interests of the user
     */
    public void setGenreOfInterests(ArrayList<BookGenres> interests) {
        this.genreOfInterests = interests;
    }

    /**
     *
     * @return a BookToInformationMap of the user's favourite books
     */
    public BookToInformationMap getFavouriteBooks() {
        return favouriteBooks;
    }

    /**
     *
     * @param book to be added to the favourite book list
     */
    public void addFavouriteBooks(Book book, String descriptionKey) {
        favouriteBooks.addBook(book.getIsbn(), descriptionKey);
    }

    /**
     * Overwrites the users favourite books list
     * @param favouriteBooks new favourite book list
     */
    public void setFavouriteBooks(BookToInformationMap favouriteBooks) {
        this.favouriteBooks = favouriteBooks;
    }

    /**
     *
     * @return UserList of all the user's friends
     */
    public UserList getFriends() {
        return friends;
    }

    /**
     *
     * @param friends new list of friends
     */
    public void setFriends(UserList friends) {
        this.friends = friends;
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
     * @param borrowRequests
     */
//    public void setBorrowRequests(BookRequestListCallback borrowRequests) {
//        this.borrowRequests = borrowRequests;
//    }

    /**
     * Required setter for Firebase
     * @param lendRequests
     */
//    public void setLendRequests(BookRequestListCallback lendRequests) {
//        this.lendRequests = lendRequests;
//    }

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


    /**
     *
     * @param genre to added to the user's genres of interest
     */
    public void addGenreOfInterest(BookGenres genre) {genreOfInterests.add(genre);}

    /**
     *
     * @param user to be added as a new friend (#no_new_friends)
     */
    public void addFriend(User user) {}

//    /**
//     *
//     * @param request new lend request to be added to lend request list
//     */
//    public void addLendRequest(BookRequest request) {
//        lendRequests.addRequest(request);
//    }

//    /**
//     *
//     * @param request new book request to be added to borrow request list
//     */
//    public void addBorrowRequest(BookRequest request) {
//        borrowRequests.addRequest(request);
//    }



    @Override
    public String toString() {
        return "User{" +
                "\nuserInfo=" + userInfo +
                ",\nownedBooks=" + ownedBooks +
                ",\nborrowedBooks=" + borrowedBooks +
                ",\nfavouriteBooks=" + favouriteBooks +
                ",\ngenreOfInterests=" + genreOfInterests +
                ",\nfriends=" + friends +
                ",\npickUpLocation="+ pickUpLocation +
                '}';
    }
}
