package com.cmput301.w19t06.theundesirablejackals;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    private Book book;
    private User owner;

    @Before
    public void setup() {
        owner = new User("Red", "IHeartGrandma",
                "Red@hood.com", "433-001-9112");
        book = new Book("The Undesirable", "Jackal The Unknown",
                "99452212-0", owner);
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