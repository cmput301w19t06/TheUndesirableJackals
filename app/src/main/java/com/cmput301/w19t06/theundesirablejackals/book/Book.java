package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.classes.Image;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds relevant information about a book
 */
public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private User owner;
    private ArrayList<BookGenres> genres;
    private ArrayList<Image> images;

    public Book(){

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
        this.images = new ArrayList<Image>();

        // adds itself to the owner's owned books
        BookList ownedBooks = owner.getOwnedBooks();
        ownedBooks.addBook(this);
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

    public ArrayList<Image> getImages() {
        return images;
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

    public ArrayList<BookGenres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<BookGenres> genres) {
        this.genres = genres;
    }

    public void addGenre(BookGenres genre) {
        genres.add(genre);
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void setStatus(BookStatus newStatus) {
        status = newStatus;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void deleteImage(Image image) {
        images.remove(image);
    }
}
