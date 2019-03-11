/**
 * A common object for holding book data
 */

package com.cmput301.w19t06.theundesirablejackals.Classes;

import com.cmput301.w19t06.theundesirablejackals.Adapter.MyBooksRecyclerViewAdapter;

public class MyBooksModelClass {



    private String title;
    private String author;
    private String isbn;
    private String status;
    private int img_book;


    public MyBooksModelClass() {
    }

    public MyBooksModelClass(String title, String author, String isbn, String status, int img_book) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.img_book = img_book;
    }


    //Getters

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        return status;
    }

    public int getImg_book() {
        return img_book;
    }

    //setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImg_book(int img_book) {
        this.img_book = img_book;
    }
}

