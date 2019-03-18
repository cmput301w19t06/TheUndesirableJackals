package com.cmput301.w19t06.theundesirablejackals.book;
import android.content.Intent;

import com.google.android.gms.common.util.BiConsumer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class helps with sorting and searching local list of books.
 * @author Art Limbaga
 * @see Book
 */
public class BookInformationList {
    private HashMap<String, Object> books;

    /**
     * Creates an empty book list
     */
    public BookInformationList() {
        books = new HashMap<String, Object>();
    }

    /**
     * Creates a new BookInformationList object with books as the initial list of the book
     * @param books
     */
    public BookInformationList(HashMap<String, Object> books) {
        this.books = books;
    }


    public int size(){
        return books.size();
    }

    public Object get(String isbn){
        return books.get(isbn);
    }

    /**
     *
     * @param informationKey Information key correlated to the book
     * @param isbn book to be added to the list
     */
    public void addBook(String isbn, String informationKey) {
        books.put(isbn, informationKey);
    }

    /**
     * TODO
     * @param bookList
     */
    public void addBooks(HashMap<String, Object> bookList){
        books.putAll(bookList);
    }

    /**
     *
     * @param book to be deleted in the book list
     */
    public void deleteBook(Book book) {
        books.remove(book.getIsbn());
    }

    /**
     *
     * @return an ArrayList of books containing all the books in the list
     */
    public HashMap<String, Object> getBooks() {
        return books;
    }



    public void setBooks(HashMap<String, Object> books){this.books = books;}


    /**
     * Checks if a book is already in the list
     * @param book to be chected in the list
     * @return false if the book is not in the list, otherwise true.
     */
    public boolean contains(Book book) {
        return books.containsKey(book.getIsbn());
    }

    @Override
    public String toString() {
        String s = "{";
        for(String b : books.keySet()){
            s += "\n" + b;
        }s += "}";
        return s;
    }
}
