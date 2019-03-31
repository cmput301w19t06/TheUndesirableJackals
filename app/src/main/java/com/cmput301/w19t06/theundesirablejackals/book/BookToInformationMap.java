package com.cmput301.w19t06.theundesirablejackals.book;


import java.util.HashMap;

/**
 * This class helps with sorting and searching local list of books.
 * @author Art Limbaga
 * @see Book
 */
public class BookToInformationMap {
    private HashMap<String, Object> books;

    /**
     * Creates an empty book list
     */
    public BookToInformationMap() {
        books = new HashMap<String, Object>();
    }

    /**
     * Creates a new BookToInformationMap object with books as the initial list of the book
     * @param books
     */
    public BookToInformationMap(HashMap<String, Object> books) {
        this.books = books;
    }


    public int size(){
        return books.size();
    }

    public String get(String isbn){
        return (String) books.get(isbn);
    }

    /**
     *
     * @param informationKey Information key correlated to the book
     * @param isbn book to be added to the list
     */
    public void addBook(String isbn, String informationKey) {
        books.put(isbn, informationKey);
    }

    /**
     * TODO
     * @param bookList
     */
    public void addBooks(HashMap<String, Object> bookList){
        books.putAll(bookList);
    }

    /**
     *
     * @param book to be deleted in the book list
     */
    public void deleteBook(Book book) {
        books.remove(book.getIsbn());
    }

    public void deleteBook(String isbn) {
        books.remove(isbn);
    }

    /**
     *
     * @return an ArrayList of books containing all the books in the list
     */
    public HashMap<String, Object> getBooks() {
        return books;
    }



    public void setBooks(HashMap<String, Object> books){this.books = books;}


    /**
     * Checks if a book is already in the list
     * @param book to be chected in the list
     * @return false if the book is not in the list, otherwise true.
     */
    public boolean contains(Book book) {
        return books.containsKey(book.getIsbn());
    }

    @Override
    public String toString() {
        String s = "{";
        for(String b : books.keySet()){
            s += "\n" + b;
        }s += "}";
        return s;
    }
}
