/*
Small test example that can be run from the terminal
*/
package com.cmput301.w19t06.theundesirablejackals;
import java.util.ArrayList;

public class Test {
    public static void main(String args[]) {
        // create user
        User felipe = new User("felipe", "pass", "email@hotmail.com", "333-333-3333");

        // check password
        Authentication authenticate = felipe.getAuthentication();
        System.out.println(authenticate.authenticate("pass"));

        // print contact information 
        ContactInformation contactInfo = felipe.getContactInformation();
        System.out.println("phone: " + contactInfo.getPhoneNumber());
        System.out.println("email: " + contactInfo.getEmail());

        // create some books 
        Book a = new Book("title 1", "author 1", "isbn 1", felipe);
        Book b = new Book("title 2", "author 2", "isbn 2", felipe);

        // add them to the user's owned books
        BookList ownedBooks = felipe.getOwnedBooks();
        ownedBooks.addBook(a);
        ownedBooks.addBook(b);

        // get the list of all owned books
        ArrayList<Book> listOfBooks = ownedBooks.getBooks();

        // print title and author of all books owned 
        for (Book i : listOfBooks) {
            String title = i.getTitle();
            String author = i.getAuthor();
            System.out.println(title + " by " + author);
        } 

        // create a second user 
        User felipeTwo = new User("felipeTwo", "pass", "email2@hotmail.com", "111-111-1111");

        // send a request for book "a"
        BookRequestHandler request = new BookRequestHandler(felipeTwo, felipe, a);

        // print requests details

        // sender name 
        String sender = ((request.getSender()).getAuthentication()).getUserName();
        System.out.println(sender);

        // receiver name
        String receiver = ((request.getReceiver()).getAuthentication()).getUserName();
        System.out.println(receiver);

        // send some messages
        Message messageOne = new Message(felipe, felipeTwo, "hello");
        Message messageTwo = new Message(felipe, felipeTwo, "I saw the request");

        // prints all messages of the receiver
        ArrayList<Message> messages = felipeTwo.getMessages();

        for (Message m : messages) {
            System.out.println(m.getText());
        }


    }
}
