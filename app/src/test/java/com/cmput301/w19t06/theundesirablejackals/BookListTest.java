package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookListTest {
    BookList bookListOne;
    BookList bookListTwo;
    ArrayList<Book> books;
    Book bookA;
    Book bookB;
    Book bookC;
    Book bookD;

    @Before
    public void setup() {
        // create three object books using the three different constructors
        bookA = new Book("Bird Box", "Josh Malerman", "9780062259660");
        bookB = new Book("The Shinning", "Stephen King", "9780307743657",
                "Fiction",
                "http://books.google.com/books/content?id=c2VHVEWUFoQC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        bookC = new Book(bookB);

        // test the empty constructor
        bookD = new Book();

        // add the data with set methods
        bookD.setCategories("Philosophy");
        bookD.setIsbn("9781554812721");
        bookD.setAuthor("Paul A. Gregory");
        bookD.setTitle("Formal Logic");
        bookD.setThumbnail("http://books.google.com/books/content?id=3hJbDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");

        // test the two different constructors
        callConstructors();
    }

    public void callConstructors() {
        bookListOne = new BookList();

        books = new ArrayList<>();
        books.add(bookC);
        books.add(bookD);

        bookListTwo = new BookList(books);
    }

    @Test
    public void getSize_isCorrect() {
        assertEquals(bookListOne.size(), (Integer) 0);
        assertEquals(bookListTwo.size(), (Integer) 2);
    }

    @Test
    public void getBooks() {
        ArrayList<Book> getBooks = bookListTwo.getBooks();

        assertEquals(getBooks.size(), 2);
        assertEquals(getBooks.contains(bookC), true);
        assertEquals(getBooks.contains(bookD), true);
        assertEquals(getBooks.contains(bookA), false);
    }

    @Test
    public void addRemove_isCorrect() {
        bookListOne.add(bookA);
        bookListOne.add(bookB);

        ArrayList<Book> getBooks = bookListOne.getBooks();
        assertEquals(getBooks.size(), 2);

        bookListOne.remove(bookA);
        bookListOne.remove(bookB);

        assertEquals(getBooks.size(), 0);
    }
}