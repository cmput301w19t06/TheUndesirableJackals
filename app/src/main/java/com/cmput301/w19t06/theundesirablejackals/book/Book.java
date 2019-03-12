package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.Image;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds relevant information regarding a published book. Information includes: title,
 * author, isbn, bookstatus, and images of the book. This class was made serializable to enable
 * safe transport of the object data to and from the database.
 * @author Art Limbaga
 * @author Felipe Rodriguez
 * @see BookStatus
 * @see Geolocation
 * @see BookGenres
 */
public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private ArrayList<BookGenres> genres;
    private ArrayList<Image> images;
    private Geolocation pickUpLocation;



    public Book(){

    }

    /**
     * Default constructor which creates a new book object
     * @param title of the book
     * @param author of the book
     * @param isbn of the book
     */
    public Book(String title, String author, String isbn) {
        // status is set to "available" as default
        // images is set to null as default
        // TODO: Images will need to be set to a default image in the future
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
        // this.owner = owner;
        this.images = new ArrayList<Image>();

        // pick up location as default at the U of A
        this.pickUpLocation = new Geolocation(53.5232, 113.5263);

        // adds itself to the owner's owned books
//        BookList ownedBooks = owner.getOwnedBooks();
//        ownedBooks.addBook(this);
    }

//    public Book(String title, String author, String isbn, User owner) {
//        // status is set to "available" as default
//        // images is set to null as default
//        // TODO: Images will need to be set to a default image in the future
//        this.title = title;
//        this.author = author;
//        this.isbn = isbn;
//        this.status = BookStatus.AVAILABLE;
//       // this.owner = owner;
//        this.images = new ArrayList<Image>();
//
//        // pick up location as default at the U of A
//        this.pickUpLocation = new Geolocation(53.5232, 113.5263);
//
//        // adds itself to the owner's owned books
//        BookList ownedBooks = owner.getOwnedBooks();
//        ownedBooks.addBook(this);
//    }

    /**
     *
     * @return title of the book in String format
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return author of the book in String format
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return ISBN of the book in String  format
     */
    public String getISBN() {
        return isbn;
    }

    public BookStatus getStatus() {
        return status;
    }

//    public User getOwner() {
//        return owner;
//    }

    /**
     *
     * @return ArrayList of book images
     */
    public ArrayList<Image> getImages() {
        return images;
    }

    /**
     *
     * @return Geolocation of the book pickup location
     */
    public Geolocation getPickUpLocation() {
        return pickUpLocation;
    }

    /**
     *
     * @param newTitle replacement title of the book
     */
    public void setTitle(String newTitle) {
        title = newTitle;
    }


    /**
     *
     * @param newAuthor replacement author of the book
     */
    public void setAuthor(String newAuthor) {
        author = newAuthor;
    }

    /**
     *
     * @param newISBN replacement ISBM of the book
     */
    public void setISBN(String newISBN) {
        isbn = newISBN;
    }

    /**
     *
     * @return ArrayList of all the book genres of the book
     */
    public ArrayList<BookGenres> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres an ArrayList of genres that the book belongs to
     */
    public void setGenres(ArrayList<BookGenres> genres) {
        this.genres = genres;
    }

    /**
     * Adds a new genre to the list of book genre that the book object belongs to
     * @param genre the genre to be added
     */
    public void addGenre(BookGenres genre) {
        genres.add(genre);
    }

    /**
     *
     * @param images an ArryList of Image objects to be assigned as images for the book
     */
    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    /**
     *
     * @param newStatus the new status of the book
     */
    public void setStatus(BookStatus newStatus) {
        status = newStatus;
    }

    /**
     *
     * @param image image to be added to the list of images of the book object
     */
    public void addImage(Image image) {
        images.add(image);
    }

    /**
     *
     * @param image image to be deleted from the list of images of the book object
     */
    public void deleteImage(Image image) {
        images.remove(image);
    }
}
