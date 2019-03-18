package com.cmput301.w19t06.theundesirablejackals.database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper{
    // database path to users
    private final static String TAG = "DatabaseHelper";

    private final static String PATH_USERS = "users";
    private final static String PATH_BOOKS = "books";
    private final static String PATH_REGISTERED = "registered";
    private final static String PATH_FAVOURITE = "favourites";
    private final static String PATH_REQUESTS = "requests";
    private final static String PATH_DESCRIPTION = "descriptions";



    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference;
    private DatabaseReference booksReference;
    private DatabaseReference registeredReference;
    private DatabaseReference favouriteReference;
    private DatabaseReference requestsReference;
    private DatabaseReference descriptionReference;
    private FirebaseUser currentUser;

    private StorageReference bookPicturesReference;
    private StorageReference userPicturesReference;





//    ~~~~~~~~~~~~~~~~~~~~~~~~~CONSTRUCTOR~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   //


    /**
     * Constructor for database helper, Requires that the user is already
     * authenticated to firebase in the calling activity
     */
    public DatabaseHelper() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();

        this.usersReference = database.getReference(PATH_USERS);
        this.booksReference = database.getReference(PATH_BOOKS);
        this.registeredReference = database.getReference(PATH_REGISTERED);
        this.favouriteReference = database.getReference(PATH_FAVOURITE);
        this.requestsReference = database.getReference(PATH_REQUESTS);
        this.descriptionReference = database.getReference(PATH_DESCRIPTION);

        this.bookPicturesReference = storageReference.getReference(PATH_BOOKS);
        this.userPicturesReference = storageReference.getReference(PATH_USERS);

    }

//~~~~~~~~~~~~~~~~~~~~~~~~~REGISTRATION~~~~~~~~~~~~~~~~~~~~~~~~~//


    /**
     * Goes to the firebase database to register (asynchronously) the currentUser's unique Username
     * @param  usernameMap The mapping between the unique Username and the data it needs to contain in the Database
     * @param  booleanCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void registerUsername(Map<String, Object> usernameMap, final BooleanCallback booleanCallback){
        registeredReference
                .child("username")
                .updateChildren(usernameMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{booleanCallback.onCallback(false);}
                    }
                });
    }

    /**
     * Goes to the firebase database to register (asynchronously) the currentUser's unique UID
     * @param  uidMap The mapping between the unique UID and the data it needs to contain in the Database
     * @param  booleanCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void registerUID(Map<String, Object> uidMap, final BooleanCallback booleanCallback){
        registeredReference
                .child("uid")
                .updateChildren(uidMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{booleanCallback.onCallback(false);}
                    }
                });
    }


    /**
     * Goes to the firebase database to register and save (asynchronously) the currentUser's custom User object
     * @param  user The currentuser's custom User object that is to be written in the database
     * @param  booleanCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    public void registerUser(final User user, final BooleanCallback booleanCallback){
        Map<String, Object> uidMap = new HashMap<>();
        Map<String, Object> tempMap = new HashMap<>();
        uidMap.put(
                currentUser.getUid(),
                user.getUserInfo().getUserName());

        tempMap.put(
                user.getUserInfo().getUserName(),
                user.getUserInfo());
        final Map<String, Object> usernameMap = new HashMap<>(tempMap);

        registerUID(uidMap, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    registerUsername(usernameMap, new BooleanCallback() {
                        @Override
                        public void onCallback(boolean bool) {
                            if(bool){
                                saveCurrentUser(user, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if(bool){
                                            booleanCallback.onCallback(true);
                                        }else { booleanCallback.onCallback(false);}
                                    }
                                });
                            }else { booleanCallback.onCallback(false); }
                        }
                    });
                }else { booleanCallback.onCallback(false); }
            }
        });

    }


    /**
     * Check if the current user (getCurrentUser) is registered
     * in the database as a full user object
     * @param booleanCallback the callback that will be used to signal that the asynchronous
     *                   database search has been completed
     */
    public void isRegistered(final BooleanCallback booleanCallback){
        registeredReference
                .child("uid")
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            booleanCallback.onCallback(true);

                        }else {
                            booleanCallback.onCallback(false);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "isRegistered ERROR HAPPENED");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    /**
     * @param username to be checked against registered usernames for uniqueness
     * @param booleanCallback the function which will be called once the result is available
     * Since database access is asynchronous, the result will not be available right away.
     * Instead, once the result comes back it can be dealt with using various Callback interfaces
     */
    public void isUsernameAvailable(String username, final BooleanCallback booleanCallback){
        registeredReference
                .child("username")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            booleanCallback.onCallback(false);

                        }else {
                            booleanCallback.onCallback(true);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Error occured in isUsernameAvaialable");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }






    //~~~~~~~~~~~USER & USERINFORMATION GETTERS~~~~~~~~~~~~~~//



    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom UserInformation object
     * @param username The username to use when searching for specific user info
     * @param  userInformationCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getUserInfoFromDatabase(String username, final UserInformationCallback userInformationCallback){
        registeredReference
                .child("username")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);
                        userInformationCallback.onCallback(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        userInformationCallback.onCallback(null);
                        Log.d(TAG, "Cancelled in getUserInfoFromDatabase");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }


    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom UserInformation object
     * @param  userInformationCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getCurrentUserInfoFromDatabase(final UserInformationCallback userInformationCallback){
        usersReference
                .child(currentUser.getUid())
                .child("userinfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);
                        userInformationCallback.onCallback(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        userInformationCallback.onCallback(null);
                        Log.d(TAG, "Cancelled in getUserInfoFromDatabase");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }



    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom User object
     * @param  userCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getCurrentUserFromDatabase(final UserCallback userCallback){
        usersReference
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
//                        Log.d(TAG, user.toString());
                        userCallback.onCallback(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Failed to retrieve User object from getCurrentUser().");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }






    //~~~~~~~~~~~~~~~~DATABASE SAVING DATA~~~~~~~~~~~~~~~~~~~~~~~~//



    /**
     * Goes to the firebase database and saves (asynchronously) the currentUser's custom User object
     * @param  user The currentuser's custom User object that is to be written/updated in the database
     * @param  booleanCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    public void saveCurrentUser(User user, final BooleanCallback booleanCallback){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put(currentUser.getUid(), user);
        usersReference
                .updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            booleanCallback.onCallback(true);
                                        }else{
                                            booleanCallback.onCallback(false);
                                        }
                                    }
                                });

    }



    //~~~~~~~~~~~~~~~~~~BOOK DESCRIPTION HANDLERS~~~~~~~~~~~~~~~~~~~~//



    public void updateBookInformation(final BookInformation bookInformation, final BooleanCallback booleanCallback){
        String tempRef;
        if(bookInformation.getBookInformationKey() == null) {
            tempRef = descriptionReference.push().getKey();
            bookInformation.setBookInformationKey(tempRef);
        }else{
            tempRef = bookInformation.getBookInformationKey();
        }
        descriptionReference
                .child(tempRef)
                .setValue(bookInformation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else {booleanCallback.onCallback(false);}
                    }
                });
    }

    public void getBookInformation(String bookInformationKey, final BookDescriptionCallback bookDescriptionCallback){
        descriptionReference
                .child(bookInformationKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            bookDescriptionCallback.onCallback(dataSnapshot.getValue(BookInformation.class));
                        }else{
                            bookDescriptionCallback.onCallback(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        bookDescriptionCallback.onCallback(null);
                        Log.d(TAG, "Something went wrong in getBookInformation");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    //~~~~~~~~~~~~~~~~~GENERIC BOOK HANDLERS~~~~~~~~~~~~~~~~~~~~~~~~~~//



    /**
     * Goes to the firebase database and fetches (asynchronously) the Book coresponding to isbn
     * @param  isbn  the string representing a valid book ISBN that google books api can use/search
     * @param  bookCallback  The callback which is passed in, to be called upon successful data acquisition
     */
    public void getBookFromDatabase(String isbn, final BookCallback bookCallback){
        booksReference
                .child(isbn)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        bookCallback.onCallback(book);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void addBookToDatabase(Book book, final BooleanCallback booleanCallback){
        booksReference
                .child(book.getIsbn())
                .setValue(book)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else {booleanCallback.onCallback(false);}
                    }
                });
    }


    //~~~~~~~~~~~~~~~~USER'S SPECIFIC BOOK HANDLERS~~~~~~~~~~~~~~~~~~~//



    /**
     * This function will update all the books that the user owns
     * @param bookInformationList  the full BookInformationList of owned books
     * @param booleanCallback
     */
    public void updateOwnedBooks(BookInformationList bookInformationList, final BooleanCallback booleanCallback){
        HashMap<String, Object> update = new HashMap<>();
        update.put("ownedBooks", bookInformationList);
        usersReference
                .child(currentUser.getUid())
                .updateChildren(update)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            booleanCallback.onCallback(true);
                        }else{
                            booleanCallback.onCallback(false);
                        }
                    }
                });
    }



    /**
     * This function will update all the user's borrowed books
     * @param bookInformationList  the full BookInformationList of borrowed books
     * @param onCallback
     */
    public void updateBorrowedBooks(BookInformationList bookInformationList, final BooleanCallback onCallback){
        HashMap<String, Object> update = new HashMap<>();
        update.put("borrowedBooks", bookInformationList);
        usersReference
                .child(currentUser.getUid())
                .updateChildren(update)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * This function will update all the user's favourite books
     * @param bookInformationList  the full BookInformationList of favourite books
     * @param onCallback
     */
    public void updateFavouriteBooks(BookInformationList bookInformationList, final BooleanCallback onCallback){
        HashMap<String, Object> update = new HashMap<>();
        update.put("favouriteBooks", bookInformationList);
        usersReference
                .child(currentUser.getUid())
                .updateChildren(update)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * This requires the owned book in order to remove the book from the database
     * @param bookInformation  the book which needs to be removed
     * @param onCallback  The callback which is used to tell the status of the database update
     */
    public void deleteOwnedBook(BookInformation bookInformation, final BooleanCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .child("ownedBooks")
                .child("books")
                .child(bookInformation.getIsbn())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * This requires the borrowed book in order to remove the book from the database
     * @param bookInformation  the book which needs to be removed
     * @param onCallback  The callback which is used to tell the status of the database update
     */
    public void deleteBorrowedBook(BookInformation bookInformation, final BooleanCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .child("borrowedBooks")
                .child("books")
                .child(bookInformation.getIsbn())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }



    /**
     * This requires the favourite book in order to remove the book from the database
     * @param bookInformation  the book which needs to be removed
     * @param onCallback  The callback which is used to tell the status of the database update
     */
    public void deleteFavouriteBook(BookInformation bookInformation, final BooleanCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .child("favouriteBooks")
                .child("books")
                .child(bookInformation.getIsbn())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~UPDATE USER DATA~~~~~~~~~~~~~~~~~~~~//


    /**
     * Goes to the firebase database to update (asynchronously) the currentUser's custom UserInfo object
     * @param  userInfo The currentuser's custom UserInfo object that is to be updated in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to the calling activity/fragment/class
     */
    public void updateUserInfo(final UserInformation userInfo, final BooleanCallback onCallback){
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put(
                "userInfo",
                userInfo);
        usersReference
                .child(currentUser.getUid())
                .updateChildren(userInfoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            updateRegisteredUserInfo(userInfo, new BooleanCallback() {
                                @Override
                                public void onCallback(boolean bool) {
                                    onCallback.onCallback(bool);
                                }
                            });

                        }else{
                            onCallback.onCallback(false);
                            Log.d(TAG, "Something went wrong updating user info");
                            if(task.getException() != null) {
                                Log.e(TAG, task.getException().toString());
                            }

                        }
                    }
                });
    }


    /**
     * Goes to the firebase database to update (asynchronously) the currentUser's custom UserInfo object
     * @param  userInfo The currentuser's custom UserInfo object that is to be updated in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void updateRegisteredUserInfo(UserInformation userInfo, final BooleanCallback onCallback){
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put(
                userInfo.getUserName(),
                userInfo);
        registeredReference
                .child("username")
                .updateChildren(userInfoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                            Log.d(TAG, "Something went wrong updating registered user info");
                            if(task.getException() != null) {
                                Log.e(TAG, task.getException().toString());
                            }
                        }
                    }
                });
    }

    //~~~~~~~~~~~~~~~~~~~CLOUD STORAGE~~~~~~~~~~~~~~~~~~~~~~~~//


    /**
     * Upload a picture related the Book book
     * @param file  the file to be uploaded to the database
     * @param bookInformation  the book that the photo needs to be attached to
     * @param onCallback  Boolean callback which gives upload status (success or not)
     */
    public void uploadBookPicture(Uri file, BookInformation bookInformation, final BooleanCallback onCallback){
        bookPicturesReference
                .child(bookInformation.getIsbn())
                .child(bookInformation.getOwner())
                .child(bookInformation.getBookPhoto())
                .putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        onCallback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onCallback(false);
                    }
                });

    }


    /**
     * Download a picture from the database related to the Book book
     * @param file  the file location to store the picture
     * @param bookInformation  The book that the picture relates to
     * @param onCallback A callback so the fetch status is known, and completion can be tracked
     */
    public void downloadBookPicture(File file, BookInformation bookInformation, final BooleanCallback onCallback){
        bookPicturesReference
                .child(bookInformation.getIsbn())
                .child(bookInformation.getOwner())
                .child(bookInformation.getBookPhoto())
                .getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        onCallback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onCallback(false);
                    }
                });
    }


    /**
     * The database helper for downloading a profile picture
     * @param file the pre-allocated file for storing the user's profile picture.
     * @param userInformation  The user who's profile picture we will be fetching
     * @param onCallback  The callback so the fetch status is known, and completion can be tracked
     */
    public void downloadProfilePicture(File file, UserInformation userInformation, final BooleanCallback onCallback){
        userPicturesReference
                .child(userInformation.getUserName())
                .child(userInformation.getUserPhoto())
                .getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        onCallback.onCallback(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onCallback(false);
                    }
                });
    }


    /**
     * Upload a user profile picture for use only by the current user
     * @param file  The file location of the file to be uploaded
     * @param userInformation  The user who is uploading their profile picture
     * @param onCallback  The call back which tells when the process is done and if it succeeded or not
     */
    public void uploadProfilePicture(Uri file, UserInformation userInformation, final BooleanCallback onCallback){
        userPicturesReference
                .child(userInformation.getUserName())
                .child(userInformation.getUserPhoto())
                .putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        onCallback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onCallback(false);
                    }
                });

    }


    //~~~~~~~~~~~~~~~~~MISC FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Get the FirebaseAuth user object that is currently in use
     * @return FirebaseAuth user object
     */
    public FirebaseUser getCurrentUser(){return currentUser;}

    /**
     * Check if the user is authenticated as a FirebaseAuth object
     * @return boolean true if the firebaseAuth object exists (logged in)
     *          boolean false otherwise
     */
    public boolean isUserLoggedin() {
        return currentUser != null;
    }


    /**
     * Sign out the current FirebaseAuth user object by calling signOut()
     * and setting currentUser to null
     */
    public void signOut() {
        firebaseAuth.signOut();
        currentUser = null;

    }


    //~~~~~~~~~~~~~~~~~~~~~REQUESTS~~~~~~~~~~~~~~~~~~~~~~~~~~//


    /**
     * Gets the user's Borrow requests (user from userInformation)
     * @param user  The user who's borrow requests we are fetching
     * @param onCallback  A callback so the fetch status is known, and completion can be tracked
     */
    public void getBorrowRequests(String user, final BookRequestListCallback onCallback){
        requestsReference
                .child(user)
                .child("borrowRequests")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            onCallback.onCallback(dataSnapshot.getValue(BookRequestList.class));

                        }else {
                            onCallback.onCallback(new BookRequestList());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCallback.onCallback(null);
                        Log.d(TAG, "isRegistered ERROR HAPPENED");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }


    /**
     * Gets the user's Lend requests (user from userInformation)
     * @param user The user who's lend requests we are fetching
     * @param onCallback  A callback so the fetch status is known, completion can be tracked, and
     *                    the data retrieved
     */
    public void getLendRequests(String user, final BookRequestListCallback onCallback){
        requestsReference
                .child(user)
                .child("lendRequests")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            onCallback.onCallback(dataSnapshot.getValue(BookRequestList.class));

                        }else {
                            onCallback.onCallback(new BookRequestList());

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCallback.onCallback(null);
                        Log.d(TAG, "isRegistered ERROR HAPPENED");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }


    /**
     * Sends a borrow request to the owner of the book
     * To be used by BORROWER ONLY
     * @param request  The book request that is being made by Borrower to Owner
     * @param onCallback A callback so the request status is known, and completion can be tracked
     */
    public void makeBorrowRequest(final BookRequest request, final BooleanCallback onCallback){
        final String owner = request.getBookRequested().getOwner();
        getLendRequests(owner, new BookRequestListCallback() {
            @Override
            public void onCallback(BookRequestList bookRequestList) {
                if(bookRequestList == null) {
                    onCallback.onCallback(false);
                    return;
                }
                bookRequestList.addRequest(request);
                setLendRequests(owner, bookRequestList, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if(bool){
                            getBorrowRequests(request.getBorrower().getUserName(), new BookRequestListCallback() {
                                @Override
                                public void onCallback(BookRequestList bookRequestList) {
                                    if(bookRequestList == null){
                                        onCallback.onCallback(false);
                                        //TODO delete lend request
                                        return;
                                    }
                                    bookRequestList.addRequest(request);
                                    setBorrowRequests(request.getBorrower(), bookRequestList, new BooleanCallback() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            onCallback.onCallback(bool);
                                        }
                                    });

                                }
                            });
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });


            }
        });

    }


    /**
     * Updates the borrower request list with new status in the book request
     * To be used by OWNER ONLY
     * @param bookRequest The book request that is being updated by Owner for Borrower
     * @param onCallback  A callback so the request status is known, and completion can be tracked
     */
    public void updateLendRequest(final Integer position, final BookRequest bookRequest, final BooleanCallback onCallback){
        getBorrowRequests(bookRequest.getBorrower().getUserName(), new BookRequestListCallback() {
            @Override
            public void onCallback(BookRequestList bookRequestList) {
                if(bookRequestList != null) {
                    for (BookRequest bookRequest1 : bookRequestList.getBookRequests()) {
                        if(bookRequest.getBookRequested().getOwner()
                                .equals(bookRequest1.getBookRequested().getOwner()) &&
                        bookRequest.getBookRequested().getIsbn()
                                .equals(bookRequest1.getBookRequested().getIsbn()))
                        {
                            Integer position1 = bookRequestList.getBookRequests().indexOf(bookRequest1);
                            updateBorrowRequest(position1, bookRequest, new BooleanCallback() {
                                @Override
                                public void onCallback(boolean bool) {
                                    if(bool){
                                        updateLendRequestAtPosition(position, bookRequest, new BooleanCallback() {
                                            @Override
                                            public void onCallback(boolean bool) {
                                                onCallback.onCallback(bool);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }


    /**
     * 
     * @param position
     * @param bookRequest
     * @param booleanCallback
     */
    private void updateLendRequestAtPosition(final Integer position, final BookRequest bookRequest, final BooleanCallback booleanCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(position.toString(), bookRequest);
        requestsReference
                .child(bookRequest.getBookRequested().getOwner())
                .child("lendRequests")
                .child("bookRequests")
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{
                            booleanCallback.onCallback(false);
                        }
                    }
                });
    }



    /**
     * Updates the borrower request at Position position using the new BookRequest bookRequest
     * @param position
     * @param bookRequest
     * @param booleanCallback
     */
    private void updateBorrowRequest(Integer position, BookRequest bookRequest, final BooleanCallback booleanCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(position.toString(), bookRequest);
        requestsReference
                .child(bookRequest.getBorrower().getUserName())
                .child("borrowRequests")
                .child("bookRequests")
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{
                            booleanCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * Sets the lender requests to the new list bookRequestList
     * @param user
     * @param bookRequestList
     * @param booleanCallback
     */
    private void setLendRequests(String user, BookRequestList bookRequestList, final BooleanCallback booleanCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lendRequests", bookRequestList);
        requestsReference
                .child(user)
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{
                            booleanCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * Sets the borrower requests to the new list bookRequestList
     * @param userInformation
     * @param bookRequestList
     * @param booleanCallback
     */
    private void setBorrowRequests(UserInformation userInformation, BookRequestList bookRequestList, final BooleanCallback booleanCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("borrowRequests", bookRequestList);
        requestsReference
                .child(userInformation.getUserName())
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            booleanCallback.onCallback(true);
                        }else{
                            booleanCallback.onCallback(false);
                        }
                    }
                });
    }
    
}
