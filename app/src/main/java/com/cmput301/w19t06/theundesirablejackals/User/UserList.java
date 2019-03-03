package com.cmput301.w19t06.theundesirablejackals.User;

import com.cmput301.w19t06.theundesirablejackals.Book.Book;
import com.cmput301.w19t06.theundesirablejackals.Book.BookGenres;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> userlist;

    public UserList() {
        userlist = new ArrayList<User>();
    }
    public UserList(ArrayList<User> users) {
        userlist = new ArrayList<User>();
    }

    public ArrayList<User> searchByInterest(BookGenres bookGenre) {
        return new ArrayList<>();
    }

    public ArrayList<User> searchByFavouriteBook(Book book) {
        return new ArrayList<>();
    }

    public ArrayList<User> doSuggestFriends(User user) {
        return new ArrayList<>();
    }

    public void add(User user) {
        userlist.add(user);
    }

    public void delete(User user) {
        userlist.remove(user);
    }

    public boolean contains(User user) {
        return userlist.contains(user);
    }
}
