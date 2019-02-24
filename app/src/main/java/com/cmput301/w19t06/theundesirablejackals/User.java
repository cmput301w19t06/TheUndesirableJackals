package com.cmput301.w19t06.theundesirablejackals;

import java.util.ArrayList;

public class User {
    private Authentication authentication;
    private ContactInformation contactInformation;
    private BookList ownedBooks;
    private BookList borrowedBooks;

    // user's requests to borrow books 
    private BookRequestList lendRequests;

    // requests to borrow user's books
    private BookRequestList borrowRequests;

    // arraylist of all mesages
    private ArrayList<Message> messages;

    private UserNotificationList notifications;

    // topics of interest
    private ArrayList<BookGenres> genreOfInterests;

    private UserList friends;

    public User(String userName, String password, String email, String phoneNumber) {
        /* Lists containing books, requests and messages are set empty when 
        creating the User instance */

        authentication = new Authentication(userName, password);
        contactInformation = new ContactInformation(email, phoneNumber);
        ownedBooks = new BookList();
        borrowedBooks = new BookList();
        lendRequests = new BookRequestList();
        borrowRequests = new BookRequestList();
        messages = new ArrayList<Message>();
        notifications = new UserNotificationList();
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public UserNotificationList getNotifications() {
        return notifications;
    }

    public void setNotifications(UserNotificationList notifications) {
        this.notifications = notifications;
    }

    public ArrayList<BookGenres> getInterests() {
        return genreOfInterests;
    }

    public void setGenreOfInterests(ArrayList<BookGenres> interests) {
        this.genreOfInterests = interests;
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

    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }

}
