package com.cmput301.w19t06.theundesirablejackals.book;
import java.util.ArrayList;
/**
 * This class helps with sorting and searching local list of books.
 * @author Art Limbaga
 * @see Book
 */
public class BookList {
    private ArrayList<Book> books;

    /**
     * Creates an empty book list
     */
    public BookList() {
        books = new ArrayList<Book>();
    }

    /**
     * Creates a new BookList object with books as the initial list of the book
     * @param books
     */
    public BookList(ArrayList<Book> books) {
        this.books = books;
    }

    /**
     * @param status to be searched in the books list
     * @return an ArrayList of books that matches the status
     */
    public ArrayList<Book> searchByStatus(BookStatus status) {
        ArrayList<Book> result = new ArrayList<Book>();

        // iterates over "books" and append instances with desired status to
        // "result" 
        for (Book b : books) {
            if (b.getStatus() == status) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     *
     * @param keyword that will be searched for in the book list.
     * @return an ArrayList of books that matches the keyword
     */
    public ArrayList<Book> searchByKeyword(String keyword) {

        return null;
    }

    /**
     *
     * @param newBook book to be added to the list
     */
    public void addBook(Book newBook) {
        books.add(newBook);
    }

    /**
     *
     * @param book to be deleted in the book list
     */
    public void deleteBook(Book book) {
        books.remove(book);
    }

    /**
     *
     * @return an ArrayList of books containing all the books in the list
     */
    public ArrayList<Book> getBooks() {
        return books;
    }


    /**
     * Checks if a book is already in the list
     * @param book to be chected in the list
     * @return false if the book is not in the list, otherwise true.
     */
    public boolean contains(Book book) {
        return books.contains(book);
    }

    @Override
    public String toString() {
        String s = "{";
        for(Book b : books){
            s += "\n" + b;
        }s += "}";
        return s;
    }
}
