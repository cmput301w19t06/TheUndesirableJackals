package com.cmput301.w19t06.theundesirablejackals.book;

import java.util.HashMap;

public class BookList {
    private HashMap<String, Book> books;

    public BookList(){}

    public BookList(HashMap<String, Book> books){this.books = books;}

    public void setBooks(HashMap<String, Book> books){this.books = books;}

    public HashMap<String, Book> getBooks(){return books;}
}
