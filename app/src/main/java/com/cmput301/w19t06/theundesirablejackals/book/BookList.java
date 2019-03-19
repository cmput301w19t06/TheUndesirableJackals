package com.cmput301.w19t06.theundesirablejackals.book;

import java.util.ArrayList;
import java.util.HashMap;

public class BookList {
    private ArrayList<Book> books;

    public BookList(){}

    public BookList(ArrayList<Book> books){this.books = books;}

    public void setBooks(ArrayList<Book> books){this.books = books;}

    public ArrayList<Book>getBooks(){return books;}

    public void add(Book book){books.add(book);}

    public void addAll(BookList bookList){
        this.books.addAll(bookList.getBooks());
    }

    public Book get(Integer integer){return books.get(integer);}

    public Integer size(){return books.size();}

    public void remove(Integer integer){books.remove(integer);}

    public void remove(Book book){books.remove(book);}
}
