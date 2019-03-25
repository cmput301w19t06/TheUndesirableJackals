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
    private String thumbnail; // holds the URL of thumbnail
    private String categories;
    private ArrayList<BookGenres> genres;




    public Book(){ }

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
        this.genres = new ArrayList<>();
    }

    public Book(String title, String author, String isbn, String categories, String thumbnail) {
        // status is set to "available" as default
        // images is set to null as default
        // TODO: Images will need to be set to a default image in the future
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.categories = categories;
        this.thumbnail = thumbnail;
        this.genres = new ArrayList<>();
    }

    public Book(Book book){
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.categories = book.getCategories();
        this.thumbnail = book.getThumbnail();
        this.genres = book.getGenres();
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
     *
     * @return thumbnail of the book in String  format
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @return Categories of the book in String  format
     */
    public String getCategories() {
        return categories;
    }

    /**
     *
     * @param newThumbnail replacement of thumbnail of the book
     */
    public void setThumbnail(String newThumbnail) {
        thumbnail = newThumbnail;
    }

    /**
     *
     * @param newCategories replacement of thumbnail of the book
     */
    public void setCategories(String newCategories) {
        categories = newCategories;
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

}
