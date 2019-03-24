package com.cmput301.w19t06.theundesirablejackals.book;

import android.net.Uri;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.io.Serializable;

public class BookInformation implements Serializable {
    private String bookInformationKey;
    private String isbn;
    private BookStatus status;
    private String bookPhoto;
    private String owner;
    private String description;

    // newly added
    private String categories;
    private String thumbnail;


    public BookInformation(BookStatus status, Uri bookPhoto, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = status;
    }

    public BookInformation(Uri bookPhoto, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
    }

    public BookInformation(BookStatus status, String isbn, String owner){
        this.isbn = isbn;
        this.owner = owner;
        this.status = status;
    }

    public BookInformation(String isbn, String owner){
        this.isbn = isbn;
//        this.bookPhoto = defaultPhotoName;
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
    }

    public BookInformation(BookStatus status, Uri bookPhoto, String description, String isbn, String owner, String categories, String thumbnail){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = status;
        this.description = description;
        this.categories = categories;
        this.thumbnail = thumbnail;
    }

    public BookInformation(Uri bookPhoto, String description, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
        this.description = description;
    }

    public BookInformation(BookStatus status, String description, String isbn, String owner, String categories, String thumbnail){
        this.isbn = isbn;
        this.owner = owner;
        this.status = status;
        this.description = description;
        this.categories = categories;
        this.thumbnail = thumbnail;
    }

    public BookInformation(String description, String isbn, String owner){
        this.isbn = isbn;
        this.description = description;
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
    }


    public BookInformation(){

    }

    public String getBookInformationKey(){return this.bookInformationKey;}

    public String getIsbn(){return isbn;}

    public BookStatus getStatus(){return status;}

    public String getBookPhoto(){return bookPhoto;}

    public String getOwner(){return owner;}

    public String getDescription(){return description;}

    public String getCategories() {return categories;}

    public String getThumbnail() {return thumbnail;}

    public void setCategories(String newCategories) {
        categories = newCategories;
    }

    public void setThumbnail(String newThumbnail) {
        thumbnail = newThumbnail;
    }

    public void setBookInformationKey(String bookInformationKey){this.bookInformationKey = bookInformationKey;}

    public void setIsbn(String isbn){this.isbn = isbn;}

    public void setDescription(String description){this.description = description;}

    public void setStatus(BookStatus status){this.status = status;}

    public void setBookPhoto(String bookPhoto){this.bookPhoto = bookPhoto;}

    private void setOwner(String owner){this.owner = owner;}
}
