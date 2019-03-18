package com.cmput301.w19t06.theundesirablejackals;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookInformationListTest {
    private BookInformationList bookInformationList;

    @Before
    public void setup() {
        bookInformationList = new BookInformationList();
    }


    @Test
    public void addBook_isCorrect(){

        Book book1 = new Book("El filibusterismo", "José Rizal", "143322211");

        // add book
        bookInformationList.addBook(book1);

        // check the book was added
        assertTrue(bookInformationList.contains(book1));

    }

    @Test
    public void deleteBook_isCorrect() {
        Book book1 = new Book("Noli me tangere", "José Rizal", "143322211");
        Book book2 = new Book("El filibusterismo", "José Rizal", "143322211");

        bookInformationList.addBook(book1);
        bookInformationList.addBook(book2);

        int sizeBefore = bookInformationList.getBooks().size();

        bookInformationList.deleteBook(book1);

        assertEquals(bookInformationList.getBooks().size(),sizeBefore - 1);
        assertFalse(bookInformationList.getBooks().contains(book1));
    }

    @Test
    public void searchBookByKeyWord_isCorrect() {

        Book book1 = new Book("Made in Abyss", "Yuki Snow", "ISBN1");
        Book book2 = new Book("Rise of the Shield Hero", "Mizu Water", "ISBN2");
        Book book3 = new Book("Solo Leveling", "Kaze Wind", "ISBN3");
        Book book4 = new Book("Promised Neverland", "Omoi Heavy", "ISBN4");
        Book book5 = new Book("Attack on Titan", "Weeabo U", "ISBN5");

        bookInformationList.addBook(book1);
        bookInformationList.addBook(book2);
        bookInformationList.addBook(book3);
        bookInformationList.addBook(book4);
        bookInformationList.addBook(book5);

        ArrayList<Book> searched =  bookInformationList.searchByKeyword("made");

        assertTrue(searched.size() == 1);
        assertTrue(searched.contains(book1));

        searched =  bookInformationList.searchByKeyword("Weeabo");

        assertTrue(searched.size() == 1);
        assertTrue(searched.contains(book5));


    }

    @Test
    public void searchBookByStatus_isCorrect() {

        Book book1 = new Book("Made in Abyss", "Yuki Snow", "ISBN1");
        Book book2 = new Book("Rise of the Shield Hero", "Mizu Water", "ISBN2");
        Book book3 = new Book("Solo Leveling", "Kaze Wind", "ISBN3");
        Book book4 = new Book("Promised Neverland", "Omoi Heavy", "ISBN4");
        Book book5 = new Book("Attack on Titan", "Weeabo U", "ISBN5");

        book2.setStatus(BookStatus.BORROWED);
        book3.setStatus(BookStatus.BORROWED);
        book4.setStatus(BookStatus.ACCEPTED);
        book5.setStatus(BookStatus.ACCEPTED);

        bookInformationList.addBook(book1);
        bookInformationList.addBook(book2);
        bookInformationList.addBook(book3);
        bookInformationList.addBook(book4);
        bookInformationList.addBook(book5);

        ArrayList<Book> searched = bookInformationList.searchByStatus(BookStatus.ACCEPTED);

        assertTrue(searched.contains(book4));
        assertTrue(searched.contains(book5));
        assertFalse(searched.contains(book1));
        assertFalse(searched.contains(book2));

    }

}
