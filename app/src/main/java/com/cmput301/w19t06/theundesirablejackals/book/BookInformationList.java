package com.cmput301.w19t06.theundesirablejackals.book;

import java.util.ArrayList;

public class BookInformationList {
    ArrayList<BookInformation> bookInformations;


    public BookInformationList(){}

    public void setBookInformations(ArrayList<BookInformation> bookInformations) {
        this.bookInformations = bookInformations;
    }

    public ArrayList<BookInformation> getBookInformations() {
        return bookInformations;
    }

    public void add(BookInformation bookInformation){bookInformations.add(bookInformation);}

    public BookInformation get(Integer integer){return bookInformations.get(integer);}

    public Integer size(){return bookInformations.size();}

    public void remove(Integer integer){bookInformations.remove(integer);}

    public void remove(BookInformation bookInformation){bookInformations.remove(bookInformation);}

}
