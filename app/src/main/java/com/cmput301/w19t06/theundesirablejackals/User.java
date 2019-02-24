package com.cmput301.w19t06.theundesirablejackals;
import java.util.ArrayList;

public class User {
    private Authentication authentication;
    private ContactInformation contactInformation;
    private BookList ownedBooks;
    private BookList borrowedBooks;

    // user's requests to borrow books 
    private ArrayList<BookRequest> lendRequests;

    // requests to borrow user's books
    private ArrayList<BookRequest> borrowRequests;

    private ArrayList<Message> messages;

    public User(String userName, String password, String email, String phoneNumber) {
        /* Lists containing books, requests and messages are set empty when 
        creating the User instance */

        authentication = new Authentication(userName, password);
        contactInformation = new ContactInformation(email, phoneNumber);
        ownedBooks = new BookList();
        borrowedBooks = new BookList();
        lendRequests = new ArrayList<BookRequest>();
        borrowRequests = new ArrayList<BookRequest>();
        messages = new ArrayList<Message>();
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

    public ArrayList<BookRequest> getLendRequests() {
        return lendRequests;
    }

    public ArrayList<BookRequest> getBorrowRequests() {
        return borrowRequests;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addLendRequest(BookRequest request) {
        lendRequests.add(request);
    }

    public void addBorrowRequest(BookRequest request) {
        borrowRequests.add(request);
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }
}
