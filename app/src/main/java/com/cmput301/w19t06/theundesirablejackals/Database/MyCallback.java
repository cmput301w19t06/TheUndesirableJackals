package com.cmput301.w19t06.theundesirablejackals.Database;

import com.cmput301.w19t06.theundesirablejackals.Book.Book;
import com.cmput301.w19t06.theundesirablejackals.Book.BookList;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.cmput301.w19t06.theundesirablejackals.User.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.User.UserList;

public interface MyCallback {
    void onCallback(User user);
    void onCallback(UserInformation userInformation);
    void onCallback(UserList userList);
    void onCallback(Book book);
    void onCallback(BookList bookList);
    void onCallback(boolean bool);
}
