package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookInformationTest {
    BookInformation bookInformation;
    BookInformation bookInformationTwo;
    String description;
    String isbn;

    @Before
    public void setup() {
        description = "Jack Torrance sees his stint as winter caretaker of a Colorado hotel as a way" +
                " back from failure, his wife sees it as a chance to preserve their family, and their " +
                "five-year-old son sees the evil waiting just for them.";
        isbn = "9780307743657";

        bookInformation = new BookInformation(BookStatus.AVAILABLE, description, isbn, "Felipe");
        bookInformationTwo = new BookInformation(bookInformation);
    }

    @Test
    public void getSetDescription_isCorrect() {
        assertEquals(bookInformation.getDescription(), description);

        String newDescription = "Book is in excellent conditions and ready to be delivered";
        bookInformation.setDescription(newDescription);

        assertEquals(bookInformation.getDescription(), newDescription);
    }

    @Test
    public void statusOwnerSetGet_isCorrect() {
        assertEquals(bookInformationTwo.getStatus(), BookStatus.AVAILABLE);
        bookInformationTwo.setStatus(BookStatus.REQUESTED);
        assertEquals(bookInformationTwo.getStatus(), BookStatus.REQUESTED);

        String newOwner = "Art";
        bookInformation.setOwner(newOwner);
        assertEquals(bookInformation.getOwner(), newOwner);
    }

    @Test
    public void setGetBookKey() {
        String key = "QWSDXCA";
        bookInformation.setBookInformationKey(key);
        assertEquals(bookInformation.getBookInformationKey(), key);
    }

    @Test
    public void setGetIsbn() {
        assertEquals(bookInformation.getIsbn(), isbn);
    }
}