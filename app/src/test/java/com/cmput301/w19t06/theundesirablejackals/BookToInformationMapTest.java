package com.cmput301.w19t06.theundesirablejackals;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BookToInformationMapTest {
    private BookToInformationMap bookToInformationMap;
    private Book bookOne;
    private Book bookTwo;

    @Before
    public void setup() {
        bookToInformationMap = new BookToInformationMap();

    }


    @Test
    public void addBook_isCorrect(){

        bookOne = new Book("Don Quijote de la Mancha", "Miguel de Cervantes",
                "9788420412146");

        bookTwo = new Book("Database Systems", "Michael Kifer", "9780321268457");


        String isbnOne = "9788420412146";
        String informationKeyOne = "ABV56";

        String informationKeyTwo = "BNM89";

        // add books
        bookToInformationMap.addBook(isbnOne, informationKeyOne);
        bookToInformationMap.addBook(bookTwo.getIsbn(), informationKeyTwo);

        // check the books were added
        assertTrue(bookToInformationMap.contains(bookOne));
        assertTrue(bookToInformationMap.contains(bookTwo));

    }

    @Test
    public void deleteBook_isCorrect() {
//        bookToInformationMap.deleteBook(bookOne);
//        assertNull(bookToInformationMap.get("9788420412146"));
    }

    @Test
    public void returnBooks_isCorrect() {
        HashMap<String, Object> returnedBooks = bookToInformationMap.getBooks();

        assertFalse(returnedBooks.containsKey("9788420412146"));
        assertFalse(returnedBooks.containsKey("9780321268457"));
    }

    @Test
    public void contains_isCorrect() {
        assertEquals(bookToInformationMap.contains(bookOne), true);
        assertEquals(bookToInformationMap.contains(bookTwo), true);

//        Book newBook = new Book("Unexistent Book", "Felipe", "9780321268333");
//
//        assertFalse(bookToInformationMap.contains(newBook));
    }

}