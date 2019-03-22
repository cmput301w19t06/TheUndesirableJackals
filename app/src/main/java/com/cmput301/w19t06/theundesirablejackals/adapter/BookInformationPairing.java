package com.cmput301.w19t06.theundesirablejackals.adapter;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;

import java.util.HashMap;

public class BookInformationPairing {
    private BookList bookList;
    private BookInformationList bookInformationList;
    private Integer size;


    public BookInformationPairing(){
        this.bookList = new BookList();
        this.bookInformationList = new BookInformationList();
        this.size = 0;
    }

    public BookInformationPairing(BookList bookList, BookInformationList bookInformationList){
        this.bookInformationList = bookInformationList;
        this.bookList = bookList;
        this.size = bookList.size();
    }

    public BookInformationList getBookInformationList() {
        return bookInformationList;
    }

    public BookList getBookList() {
        return bookList;
    }

    public void setBookInformationList(BookInformationList bookInformationList) {
        this.bookInformationList = bookInformationList;
        this.size = bookInformationList.size();
    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;
        this.size = bookList.size();
    }

    public Book getBook(Integer integer){
        return bookList.get(integer);
    }

    public BookInformation getInformation(Integer integer){
        return bookInformationList.get(integer);
    }


    public void addPair(Book book, BookInformation bookInformation){
        this.bookInformationList.add(bookInformation);
        this.bookList.add(book);
        this.size += 1;
    }

    public void addSingle(Book book){
        this.bookList.add(book);
        this.bookInformationList.add(new BookInformation(book.getIsbn(), "ANON"));
        this.size += 1;
    }


    public void addAll(BookInformationPairing newData){
        this.size += newData.size();
        this.bookList.addAll(newData.getBookList());
        this.bookInformationList.addAll(newData.getBookInformationList());
    }

    public Integer size(){return this.size;}

    public void remove(Integer integer){
        this.bookList.remove(integer);
        this.bookInformationList.remove(integer);
    }
}
