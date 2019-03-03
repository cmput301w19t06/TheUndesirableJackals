package com.cmput301.w19t06.theundesirablejackals;
import com.cmput301.w19t06.theundesirablejackals.Book.Book;
import com.cmput301.w19t06.theundesirablejackals.Book.BookList;
import com.cmput301.w19t06.theundesirablejackals.Book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.User.User;

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
        User felipe = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");

        // re-written test to check the book was added to the owner's list instead of the "bookList"

        // create a new book and add it to the owner's list
        Book book1 = new Book("Title1", "Author1", "ISBN1", felipe);

        // retrieve the owner's list of books owned
        BookList ownedBooks = felipe.getOwnedBooks();

        // check the book was added
        assertTrue(ownedBooks.getBooks().contains(book1));


//        int sizeBefore = bookList.getBooks().size();
//
//        bookList.addBook(book1);
//
//        assertTrue(bookList.getBooks().contains(book1));
//        assertEquals(bookList.getBooks().size(), sizeBefore + 1);
//
//        // adding a null book
//        book1 = null;
//
//        sizeBefore = bookList.getBooks().size();
//
//        bookList.addBook(book1);
//        assertFalse(bookList.getBooks().contains(book1));
//        assertEquals(bookList.getBooks().size(), sizeBefore);
//
//        // adding the same book to the same list
//        book1 = new Book("Title1", "Author1", "ISBN1", felipe);
//
//        sizeBefore = bookList.getBooks().size();
//
//        bookList.addBook(book1);
//
//        assertTrue(bookList.getBooks().contains(book1));
//        assertEquals(bookList.getBooks().size(), sizeBefore);

    }

    @Test
    public void deleteBook_isCorrect() {
        // TO BE IMPLEMENTED Test will be unsatisfactory

        User felipe = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");
        Book book1 = new Book("Title1", "Author1", "ISBN1", felipe);
        Book book2 = new Book("Title2", "Author2", "ISBN2", felipe);

        bookList.addBook(book1);
        bookList.addBook(book2);

        int sizeBefore = bookList.getBooks().size();

        bookList.deleteBook(book1);

        assertEquals(bookList.getBooks().size(),sizeBefore - 1);
        assertFalse(bookList.getBooks().contains(book1));
    }

    @Test
    public void searchBookByKeyWord_isCorrect() {
        User felipe = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");
        Book book1 = new Book("Made in Abyss", "Yuki Snow", "ISBN1", felipe);
        Book book2 = new Book("Rise of the Shield Hero", "Mizu Water", "ISBN2", felipe);
        Book book3 = new Book("Solo Leveling", "Kaze Wind", "ISBN3", felipe);
        Book book4 = new Book("Promised Neverland", "Omoi Heavy", "ISBN4", felipe);
        Book book5 = new Book("Attack on Titan", "Weeabo U", "ISBN5", felipe);

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
        User felipe = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");
        Book book1 = new Book("Title1", "Author1", "ISBN1", felipe);
        Book book2 = new Book("Title2", "Author2", "ISBN2", felipe);
        Book book3 = new Book("Title3", "Author3", "ISBN3", felipe);
        Book book4 = new Book("Title4", "Author4", "ISBN4", felipe);
        Book book5 = new Book("Title5", "Author5", "ISBN5", felipe);

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
