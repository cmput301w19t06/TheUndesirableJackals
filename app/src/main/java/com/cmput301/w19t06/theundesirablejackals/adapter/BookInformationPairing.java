package com.cmput301.w19t06.theundesirablejackals.adapter;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;

import java.util.HashMap;

public class BookInformationPairing {
    private BookList bookList;
    private BookInformationList bookInformationList;

    public BookInformationPairing(){
        this.bookList = new BookList();
        this.bookInformationList = new BookInformationList();

    }

    public BookInformationPairing(BookList bookList, BookInformationList bookInformationList){
        this.bookInformationList = bookInformationList;
        this.bookList = bookList;
    }

    public BookInformationList getBookInformationList() {
        return bookInformationList;
    }

    public BookList getBookList() {
        return bookList;
    }

    public void setBookInformationList(BookInformationList bookInformationList) {
        this.bookInformationList = bookInformationList;

    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;

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

    }

    public void addSingle(Book book){
        this.bookList.add(book);
        this.bookInformationList.add(new BookInformation(BookStatus.UNKNOWN, book.getIsbn(), "ANON"));
    }

    public void addOwner(Book book, BookInformation bookInformation) {
        book.setIsbn("SET_TO_OWNER");
        this.bookList.add(book);
        this.bookInformationList.add(bookInformation);
    }


    public void addAll(BookInformationPairing newData){

        this.bookList.addAll(newData.getBookList());
        this.bookInformationList.addAll(newData.getBookInformationList());
    }

    public Integer size(){return bookList.size();}

    public void remove(Integer integer){
        this.bookList.remove(integer);
        this.bookInformationList.remove(integer);

    }
}
