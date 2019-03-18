package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

public class BookInformation {
    private String bookInformationKey;
    private String isbn;
    private BookStatus status;
    private String bookPhoto;
    private String owner;
    private String description;


    public BookInformation(BookStatus status, String bookPhoto, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto;
        this.owner = owner;
        this.status = status;
    }

    public BookInformation(String bookPhoto, String isbn, String owner){
        this.isbn = isbn;
        this.bookPhoto = bookPhoto;
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


    public BookInformation(){

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

    private void setOwner(String owner){this.owner = owner;}
}
