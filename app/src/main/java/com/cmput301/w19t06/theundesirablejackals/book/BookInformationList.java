package com.cmput301.w19t06.theundesirablejackals.book;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores Book information in an ArrayList for ease of access. Serializable is implemented to enable
 * data transfer of type BookInformationList across multiple activities
 * @author Art Limbaga
 */
public class BookInformationList implements Serializable {
    ArrayList<BookInformation> bookInformations;


    public BookInformationList(){this.bookInformations = new ArrayList<BookInformation>();}

    public void setBookInformations(ArrayList<BookInformation> bookInformations) {
        this.bookInformations = bookInformations;
    }

    public ArrayList<BookInformation> getBookInformations() {
        return bookInformations;
    }

    public void add(BookInformation bookInformation){bookInformations.add(bookInformation);}

    public void addAll(BookInformationList bookInformationList){
        this.bookInformations.addAll(bookInformationList.getBookInformations());
    }

    public BookInformation get(Integer integer){return bookInformations.get(integer);}

    public Integer size(){return bookInformations.size();}

    public void remove(Integer integer){bookInformations.remove(integer);}

    public void remove(BookInformation bookInformation){bookInformations.remove(bookInformation);}

}
