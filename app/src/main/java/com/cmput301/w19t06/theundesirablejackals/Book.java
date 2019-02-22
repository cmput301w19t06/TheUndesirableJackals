package com.cmput301.w19t06.theundesirablejackals;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private User owner;
    private Image image;

    public enum  BookStatus {
        AVAILABLE,  // book is available for all users to be borrowed
        REQUESTED,  // book is requested by a user
        ACCEPTED,   // book request has been accepted by the owner
        BORROWED    // book was handed off to the borrower
    }

    public Book(String title, String author, String isbn, User owner) {
        // status is set to "available" as default
        // images is set to null as default
        // TODO: Images will need to be set to a default image in the future
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
        this.owner = owner;
        this.image = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return isbn;
    }

    public BookStatus getStatus() {
        return status;
    }

    public User getOwner() {
        return owner;
    }

    public Image getImage() {
        return image;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setAuthor(String newAuthor) {
        author = newAuthor;
    }

    public void setISBN(String newISBN) {
        isbn = newISBN;
    }

    public void setStatus(BookStatus newStatus) {
        status = newStatus;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void deleteImage() {
        image = null;
    }
}
