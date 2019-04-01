package com.cmput301.w19t06.theundesirablejackals.book;

import android.net.Uri;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.io.Serializable;

/**
 * This class holds book information of a book with respect to who owns the book.
 * @author Devon Deweert
 * @author Art Limbaga
 * @see Book
 */
public class BookInformation implements Serializable {
    private String bookInformationKey;
    private String isbn;
    private BookStatus status;
    private String bookPhoto;
    private String owner;
    private String description;


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

    public BookInformation(BookStatus status, Uri bookPhoto, String description, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = status;
        this.description = description;
    }

    public BookInformation(Uri bookPhoto, String description, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto.getLastPathSegment();
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
        this.description = description;
    }

    public BookInformation(BookStatus status, String description, String isbn, String owner){
        this.isbn = isbn;
        this.owner = owner;
        this.status = status;
        this.description = description;
    }

    public BookInformation(String description, String isbn, String owner){
        this.isbn = isbn;
        this.description = description;
        this.owner = owner;
        this.status = BookStatus.AVAILABLE;
    }


    public BookInformation(){
    }

    public BookInformation(BookInformation bookInformation){
        this.bookInformationKey = bookInformation.getBookInformationKey();
        this.isbn = bookInformation.getIsbn();
        this.status = bookInformation.getStatus();
        this.bookPhoto = bookInformation.getBookPhoto();
        this.owner = bookInformation.getOwner();
        this.description = bookInformation.getDescription();
    }


    public String getBookInformationKey(){return this.bookInformationKey;}

    public String getIsbn(){return isbn;}

    public BookStatus getStatus(){return status;}

    public String getBookPhoto(){return bookPhoto;}

    public String getOwner(){return owner;}

    public String getDescription(){return description;}

    public void setBookInformationKey(String bookInformationKey){this.bookInformationKey = bookInformationKey;}

    public void setIsbn(String isbn){this.isbn = isbn;}

    public void setDescription(String description){this.description = description;}

    public void setStatus(BookStatus status){this.status = status;}

    public void setBookPhoto(String bookPhoto){this.bookPhoto = bookPhoto;}

    public void setOwner(String owner){this.owner = owner;}

    public void setBookPhotoByUri(Uri uri){this.bookPhoto = uri.getLastPathSegment();}
}
