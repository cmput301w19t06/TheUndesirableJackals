package com.cmput301.w19t06.theundesirablejackals.book;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.Image;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

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
    private String description;
    private BookStatus status;
    private ArrayList<BookGenres> genres;
    private String bookPhoto;
    private UserInformation owner;


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
        this.genres = new ArrayList<>();
        this.bookPhoto = "";
    }

    /**
     * Default constructor which creates a new book object
     * @param title of the book
     * @param author of the book
     * @param isbn of the book
     */
    public Book(String title, String author, String isbn, String description) {
        // status is set to "available" as default
        // images is set to null as default
        // TODO: Images will need to be set to a default image in the future
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.status = BookStatus.AVAILABLE;
        this.genres = new ArrayList<BookGenres>();


    }

    /**
     * Book constructor with all the members of the book class
     * @param title
     * @param author
     * @param isbn
     * @param owner
     * @param bookPhoto
     */
    public Book(String title, String author, String isbn, UserInformation owner, String bookPhoto) {

        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
        this.genres = new ArrayList<>();
        this.owner = owner;
        this.bookPhoto = bookPhoto;

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
     * @return  Return user's the description of the book
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description  the user's description of the book
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the owner of this book object
     * @return the User object who is the owner of the book
     */
    public UserInformation getOwner() {
        return owner;
    }


    /**
     * Set the owner of this book object
     * @param owner  the User object who is the owner of the book
     */
    public void setOwner(UserInformation owner) {
        this.owner = owner;
    }


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
    public String getIsbn() {
        return isbn;
    }


    /**
     * get this book's lend status (enum)
     * @return this book's current lend status
     */
    public BookStatus getStatus() {
        return status;
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
     * @param newISBN replacement ISBN of the book
     */
    public void setIsbn(String newISBN) {
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
     * @param newStatus the new status of the book
     */
    public void setStatus(BookStatus newStatus) {
        status = newStatus;
    }

    public void deleteBookPhoto() {
        bookPhoto = "";
    }


    public String getBookPhoto() {
        return bookPhoto;
    }

    public void setBookPhoto(String bookPhoto) {
        this.bookPhoto = bookPhoto;
    }


}
