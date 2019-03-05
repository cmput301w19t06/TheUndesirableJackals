package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.Book.Book;
import com.cmput301.w19t06.theundesirablejackals.Book.BookGenres;
import com.cmput301.w19t06.theundesirablejackals.Book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.User.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookTest {
    private Book book;
    private User owner;
    private ArrayList<BookGenres> genres;
    @Before
    public void setup() {
        owner = new User("Red", "IHeartGrandma",
                "Red@hood.com", "433-001-9112");
        book = new Book("The Undesirable", "Jackal The Unknown",
                "99452212-0", owner);

        genres = new ArrayList<>();

        genres.add(BookGenres.SATIRE);
        genres.add(BookGenres.FANTASY);

    }
    @Test
    public void setTitle_isCorrect() {
        book.setTitle("The Undesirable Part 1");
        assertEquals(book.getTitle(),"The Undesirable Part 1" );
    }

    @Test
    public void setAuthor_isCorrect() {
        book.setAuthor("Jackal the Wellknown");
        assertEquals(book.getAuthor(),"Jackal the Wellknown" );
    }

    @Test
    public void setISBN_isCorrect() {
        book.setISBN("178238-011");
        assertEquals(book.getISBN(),"178238-011" );
    }

    @Test
    public void setGenres_isCorrect() {
        book.setGenres(genres);
        assertEquals(book.getGenres(), genres);
    }

    @Test
    public void addGenre_isCorrect() {
        book.setGenres(genres);
        book.addGenre(BookGenres.ANTHOLOGY);
        assertTrue(book.getGenres().contains(BookGenres.ANTHOLOGY));
    }

    @Test
    public void setStatus_isCorrect() {
        book.setStatus(BookStatus.BORROWED);
        assertEquals(book.getStatus(), BookStatus.BORROWED );
    }

    @Test
    public void addImage_isCorrect() {
        Image newImage = new Image("filesystem/folder/secretpics/tuj.png");
        book.addImage(newImage);
        assertTrue(book.getImages().contains(newImage));
    }

    @Test
    public void deleteImage_isCorrect() {
        Image newImage = new Image("filesystem/folder/secretpics/tuj.png");
        book.addImage(newImage);
        book.deleteImage(newImage);
        assertFalse(book.getImages().contains(newImage));
    }
}