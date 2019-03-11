package com.cmput301.w19t06.theundesirablejackals;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookListTest {
    private BookList bookList;

    @Before
    public void setup() {
        bookList = new BookList();
    }


    @Test
    public void addBook_isCorrect(){

        Book book1 = new Book("El filibusterismo", "José Rizal", "143322211");

        // add book
        bookList.addBook(book1);

        // check the book was added
        assertTrue(bookList.contains(book1));

    }

    @Test
    public void deleteBook_isCorrect() {
        Book book1 = new Book("Noli me tangere", "José Rizal", "143322211");
        Book book2 = new Book("El filibusterismo", "José Rizal", "143322211");

        bookList.addBook(book1);
        bookList.addBook(book2);

        int sizeBefore = bookList.getBooks().size();

        bookList.deleteBook(book1);

        assertEquals(bookList.getBooks().size(),sizeBefore - 1);
        assertFalse(bookList.getBooks().contains(book1));
    }

    @Test
    public void searchBookByKeyWord_isCorrect() {

        Book book1 = new Book("Made in Abyss", "Yuki Snow", "ISBN1");
        Book book2 = new Book("Rise of the Shield Hero", "Mizu Water", "ISBN2");
        Book book3 = new Book("Solo Leveling", "Kaze Wind", "ISBN3");
        Book book4 = new Book("Promised Neverland", "Omoi Heavy", "ISBN4");
        Book book5 = new Book("Attack on Titan", "Weeabo U", "ISBN5");

        bookList.addBook(book1);
        bookList.addBook(book2);
        bookList.addBook(book3);
        bookList.addBook(book4);
        bookList.addBook(book5);

        ArrayList<Book> searched =  bookList.searchByKeyword("made");

        assertTrue(searched.size() == 1);
        assertTrue(searched.contains(book1));

        searched =  bookList.searchByKeyword("Weeabo");

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

        bookList.addBook(book1);
        bookList.addBook(book2);
        bookList.addBook(book3);
        bookList.addBook(book4);
        bookList.addBook(book5);

        ArrayList<Book> searched = bookList.searchByStatus(BookStatus.ACCEPTED);

        assertTrue(searched.contains(book4));
        assertTrue(searched.contains(book5));
        assertFalse(searched.contains(book1));
        assertFalse(searched.contains(book2));

    }

}
