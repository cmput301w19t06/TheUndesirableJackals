package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    private Book bookA;
    private Book bookB;
    private Book bookC;

    @Before
    public void setup() {
        // create three object books using the three different constructors
        bookA = new Book("Bird Box", "Josh Malerman", "9780062259660");
        bookB = new Book("The Shinning", "Stephen King", "9780307743657",
                "Fiction",
                "http://books.google.com/books/content?id=c2VHVEWUFoQC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        bookC = new Book(bookB);

    }

    @Test
    public void getSetTitle_isCorrect() {
        assertEquals(bookC.getTitle(), "The Shinning");
        assertEquals(bookA.getTitle(), "Bird Box");
        bookA.setTitle("Change");
        assertEquals(bookA.getTitle(), "Change");
    }

    @Test
    public void getSetAuthor_isCorrect() {
        assertEquals(bookB.getAuthor(), "Stephen King");
        bookA.setAuthor("King");
        assertEquals(bookA.getAuthor(), "King");
    }

    @Test
    public void getSetISBN_isCorrect() {
        assertEquals(bookB.getIsbn(), "9780307743657");
        bookB.setIsbn("9780307743658");
        assertEquals(bookB.getIsbn(), "9780307743658");
    }

    @Test
    public void getSetThumbnail_isCorrect() {
        assertEquals(bookC.getThumbnail(),
                "http://books.google.com/books/content?id=c2VHVEWUFoQC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        bookC.setIsbn(null);
        assertEquals(bookC.getIsbn(), null);
    }

    @Test
    public void getSetCategories_isCorrect() {
        assertEquals(bookB.getCategories(), "Fiction");
        bookC.setCategories("Mistery & Horror");
        assertEquals(bookC.getCategories(), "Mistery & Horror");
    }
}