package com.cmput301.w19t06.theundesirablejackals;

import java.util.ArrayList;

public class BookList {
    private ArrayList<Book> books;

    public BookList() {
        books = new ArrayList<Book>();
    }

    public BookList(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> searchByStatus(Book.BookStatus status) {
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
    public ArrayList<Book> searchByKeyword(String keyword) {

        return null;
    }

    public void addBook(Book newBook) {
        books.add(newBook);
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }


}
