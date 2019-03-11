package com.cmput301.w19t06.theundesirablejackals.user;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookGenres;

import java.util.ArrayList;

/**
 * This class will handle a local list of users for searching, sorting, and filtering. Will be used
 * for friends list and suggested friends list.
 * @author  Art Limbaga
 * @see User
 */
public class UserList {
    private ArrayList<User> userlist;

    /**
     * Create an empty user list
     */
    public UserList() {
        userlist = new ArrayList<User>();
    }

    /**
     * Creates a user list with predifined users
     * @param users user to be part of the user list
     */
    public UserList(ArrayList<User> users) {
        userlist = new ArrayList<User>();
    }

    /**
     *
     * @return an ArrayList of all the users in the list
     */
    public ArrayList<User> getUserlist(){return userlist;}

    /**
     * Searches the list of users based on book genre of interest
     * @param bookGenre the genre of interest
     * @return an ArrayList of users that are interested in the genre of interest
     */
    public ArrayList<User> searchByInterest(BookGenres bookGenre) {
        return new ArrayList<>();
    }

    /**
     * Searches the list of the users based on their favourite books
     * @param book favourite book
     * @return An ArrayList of users with the book parameter as one of their favourite books
     */
    public ArrayList<User> searchByFavouriteBook(Book book) {
        return new ArrayList<>();
    }

    /**
     *
     * @param user that will be suggested friends based on user's interests and favourite books
     * @return An ArrayList of Users that could potentially be the user's interest
     */
    public ArrayList<User> doSuggestFriends(User user) {
        return new ArrayList<>();
    }

    /**
     *
     * @param user to be added to the UserList
     */
    public void add(User user) {
        userlist.add(user);
    }

    /**
     *
     * @param user to be deleted from the UserList
     */
    public void delete(User user) {
        userlist.remove(user);
    }

    /**
     *
     * @param user to be checked if it exists in the list
     * @return false if the user is not in the list, true otherwise.
     */
    public boolean contains(User user) {
        return userlist.contains(user);
    }
}
