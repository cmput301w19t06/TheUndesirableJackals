package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.Book.Book;
import com.cmput301.w19t06.theundesirablejackals.Book.BookGenres;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.cmput301.w19t06.theundesirablejackals.User.UserList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserListTest {
    private UserList userList;
    private User user1, user2, user3, user4, user5;
    private Book book;

    @Before
    public void setup() {
        user1 = new User("user1", "passu1",
                "user1@hotmail.com", "333-333-4441");
        user2 = new User("user2", "passu2",
                "user2@hotmail.com", "333-333-4442");
        user3 = new User("user3", "passu3",
                "user3@hotmail.com", "333-333-4443");
        user4 = new User("user4", "passu4",
                "user4@hotmail.com", "333-333-4444");
        user5 = new User("user5", "passu5",
                "user5@hotmail.com", "333-333-4445");
        book = new Book("The Undesirable", "Jackal The Unknown",
                "99452212-0", user1);

        user1.addGenreOfInterest(BookGenres.GRAPHIC_NOVEL);
        user2.addGenreOfInterest(BookGenres.AUTOBIOGRAPHY);
        user3.addGenreOfInterest(BookGenres.GRAPHIC_NOVEL);
        user4.addGenreOfInterest(BookGenres.ANTHOLOGY);
        user5.addGenreOfInterest(BookGenres.CRIME);

        user1.addFavouriteBooks(book);
        user2.addFavouriteBooks(book);
        user3.addFavouriteBooks(book);

        userList = new UserList();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);


    }

    @Test
    public void searchByInterest_isCorrect() {
        ArrayList<User> result = userList.searchByInterest(BookGenres.GRAPHIC_NOVEL);
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user3));


    }

    @Test
    public void searchByFavouriteBook_isCorrect() {
        ArrayList<User> result = userList.searchByFavouriteBook(book);
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        assertTrue(result.contains(user3));
    }

    @Test
    public void doSuggestFriends_isCorrect() {
        ArrayList<User> result = userList.doSuggestFriends(user1);

        // same favourite book
        assertTrue(result.contains(user2));

        // same topics of interest
        assertTrue(result.contains(user3));

    }
}