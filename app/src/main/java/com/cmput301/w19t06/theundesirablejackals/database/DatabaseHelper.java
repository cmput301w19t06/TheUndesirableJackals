package com.cmput301.w19t06.theundesirablejackals.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper{
    // database path to users
    private final static String TAG = "DatabaseHelper";

    private final static String PATH_USERS = "users";
    private final static String PATH_BOOKS = "globalBooks";
    private final static String PATH_REGISTERED = "registered";
    private final static String PATH_FAVOURITE = "favourites";
    private final static String PATH_REQUESTS = "requests";


    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference;
    private DatabaseReference booksReference;
    private DatabaseReference registeredReference;
    private DatabaseReference favouriteReference;
    private DatabaseReference requestsReference;
    private FirebaseUser currentUser;





//    ~~~~~~~~~~~~~~~~~~~~~~~~~CONSTRUCTOR~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   //


    /**
     * Constructor for database helper, Requires that the user is already
     * authenticated to firebase in the calling activity
     */
    public DatabaseHelper() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.usersReference = database.getReference(PATH_USERS);
        this.booksReference = database.getReference(PATH_BOOKS);
        this.registeredReference = database.getReference(PATH_REGISTERED);
        this.favouriteReference = database.getReference(PATH_FAVOURITE);
        this.requestsReference = database.getReference(PATH_REQUESTS);

    }

//~~~~~~~~~~~~~~~~~~~~~~~~~REGISTRATION~~~~~~~~~~~~~~~~~~~~~~~~~//


    /**
     * Goes to the firebase database to register (asynchronously) the currentUser's unique Username
     * @param  usernameMap The mapping between the unique Username and the data it needs to contain in the Database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void registerUsername(Map<String, Object> usernameMap, final BooleanCallback onCallback){
        registeredReference
                .child("username")
                .updateChildren(usernameMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{onCallback.onCallback(false);}
                    }
                });
    }

    /**
     * Goes to the firebase database to register (asynchronously) the currentUser's unique UID
     * @param  uidMap The mapping between the unique UID and the data it needs to contain in the Database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void registerUID(Map<String, Object> uidMap, final BooleanCallback onCallback){
        registeredReference
                .child("uid")
                .updateChildren(uidMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{onCallback.onCallback(false);}
                    }
                });
    }


    /**
     * Goes to the firebase database to register and save (asynchronously) the currentUser's custom User object
     * @param  user The currentuser's custom User object that is to be written in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    public void registerUser(final User user, final BooleanCallback onCallback){
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
                                            onCallback.onCallback(true);
                                        }else { onCallback.onCallback(false);}
                                    }
                                });
                            }else { onCallback.onCallback(false); }
                        }
                    });
                }else { onCallback.onCallback(false); }
            }
        });

    }


    /**
     * Check if the current user (getCurrentUser) is registered
     * in the database as a full user object
     * @param onCallback the callback that will be used to signal that the asynchronous
     *                   database search has been completed
     */
    public void isRegistered(final BooleanCallback onCallback){
        registeredReference
                .child("uid")
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            onCallback.onCallback(true);

                        }else {
                            onCallback.onCallback(false);

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
     * @param onCallback the function which will be called once the result is available
     * Since database access is asynchronous, the result will not be available right away.
     * Instead, once the result comes back it can be dealt with using various Callback interfaces
     */
    public void isUsernameAvailable(String username, final BooleanCallback onCallback){
        registeredReference
                .child("username")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            onCallback.onCallback(false);

                        }else {
                            onCallback.onCallback(true);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Error occured in isUsernameAvaialable");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }





    //~~~~~~~~~~~DATABASE ACCESS & GETTERS~~~~~~~~~~~~~~//



    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom UserInformation object
     * @param username The username to use when searching for specific user info
     * @param  onCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getUserInfoFromDatabase(String username, final UserInformationCallback onCallback){
        registeredReference
                .child("username")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);
                        onCallback.onCallback(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCallback.onCallback(null);
                        Log.d(TAG, "Cancelled in getUserInfoFromDatabase");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }


    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom UserInformation object
     * @param  onCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getCurrentUserInfoFromDatabase(final UserInformationCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .child("userinfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);
                        onCallback.onCallback(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCallback.onCallback(null);
                        Log.d(TAG, "Cancelled in getUserInfoFromDatabase");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }



    /**
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom User object
     * @param  onCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getCurrentUserFromDatabase(final UserCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
//                        Log.d(TAG, user.toString());
                        onCallback.onCallback(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Failed to retrieve User object from getCurrentUser().");
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }


    /**
     * Goes to the firebase database and fetches (asynchronously) the Book coresponding to isbn
     * @param  isbn  the string representing a valid book ISBN that google books api can use/search
     * @param  callback  The callback which is passed in, to be called upon successful data acquisition
     */
    public void getBookFromDatabase(String isbn, final BookCallback callback){
        booksReference
                .child(isbn)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        callback.onCallback(book);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }




    /**
     * Get the FirebaseAuth user object that is currently in use
     * @return FirebaseAuth user object
     */
    public FirebaseUser getCurrentUser(){return currentUser;}




    //~~~~~~~~~~~~~~~~DATABASE SAVING DATA~~~~~~~~~~~~~~~~~~~~~~~~//



    /**
     * Goes to the firebase database and saves (asynchronously) the currentUser's custom User object
     * @param  user The currentuser's custom User object that is to be written/updated in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    public void saveCurrentUser(User user, final BooleanCallback onCallback){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put(currentUser.getUid(), user);
        usersReference
                .updateChildren(userMap)
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
     * This function will update all the books that the user owns
     * @param bookList  the full BookList of owned books
     * @param onCallback
     */
    public void saveCurrentUsersOwnedBooks(BookList bookList, final BooleanCallback onCallback){
        HashMap<String, Object> ownedBookHashMap = new HashMap<>();
        ownedBookHashMap.put("ownedBooks", bookList);
        usersReference
                .child(currentUser.getUid())
                .updateChildren(ownedBookHashMap)
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
     * This requires the whole Owned book list in order to remove the book(s) that are no longer owned
     * this is due to firebase restrictions, and the method in which it updates data
     * @param bookList  the new list which has already been altered (removed the book data)
     * @param onCallback  The callback which is used to tell the status of the database update
     */
    public void deleteCurrentUsersOwnedBook(BookList bookList, final BooleanCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .child("ownedBooks")
                .child("books")
                .setValue(bookList)
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


   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~DATABASE UPDATE DATA~~~~~~~~~~~~~~~~~~~~//


    /**
     * Goes to the firebase database to update (asynchronously) the currentUser's custom UserInfo object
     * @param  userInfo The currentuser's custom UserInfo object that is to be updated in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
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
                                    if(bool){
                                        onCallback.onCallback(true);
                                    }else {
                                        onCallback.onCallback(false);

                                    }
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





    //~~~~~~~~~~~~~~~~~MISC FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~//



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
     * @param userInformation
     * @param onCallback
     */
    public void getBorrowRequests(UserInformation userInformation, final BookRequestListCallback onCallback){
        requestsReference
                .child(userInformation.getUserName())
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
     * @param userInformation
     * @param onCallback
     */
    public void getLendRequests(UserInformation userInformation, final BookRequestListCallback onCallback){
        requestsReference
                .child(userInformation.getUserName())
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
     * @param request
     * @param onCallback
     */
    public void makeBorrowRequest(final BookRequest request, final BooleanCallback onCallback){
        final UserInformation owner = request.getBookRequested().getOwner();
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
                            getBorrowRequests(request.getBorrower(), new BookRequestListCallback() {
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
     * @param bookRequest
     * @param onCallback
     */
    public void updateLendRequest(final Integer position, final BookRequest bookRequest, final BooleanCallback onCallback){
        getBorrowRequests(bookRequest.getBorrower(), new BookRequestListCallback() {
            @Override
            public void onCallback(BookRequestList bookRequestList) {
                if(bookRequestList != null) {
                    for (BookRequest bookRequest1 : bookRequestList.getBookRequests()) {
                        if(bookRequest.getBookRequested().getOwner().getUserName()
                                .equals(bookRequest1.getBookRequested().getOwner().getUserName()) &&
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
     * @param onCallback
     */
    private void updateLendRequestAtPosition(final Integer position, final BookRequest bookRequest, final BooleanCallback onCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(position.toString(), bookRequest);
        requestsReference
                .child(bookRequest.getBookRequested().getOwner().getUserName())
                .child("lendRequests")
                .child("bookRequests")
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }



    /**
     * Updates the borrower request at Position position using the new BookRequest bookRequest
     * @param position
     * @param bookRequest
     * @param onCallback
     */
    private void updateBorrowRequest(Integer position, BookRequest bookRequest, final BooleanCallback onCallback){
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
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * Sets the lender requests to the new list bookRequestList
     * @param userInformation
     * @param bookRequestList
     * @param onCallback
     */
    private void setLendRequests(UserInformation userInformation, BookRequestList bookRequestList, final BooleanCallback onCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lendRequests", bookRequestList);
        requestsReference
                .child(userInformation.getUserName())
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }


    /**
     * Sets the borrower requests to the new list bookRequestList
     * @param userInformation
     * @param bookRequestList
     * @param onCallback
     */
    private void setBorrowRequests(UserInformation userInformation, BookRequestList bookRequestList, final BooleanCallback onCallback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("borrowRequests", bookRequestList);
        requestsReference
                .child(userInformation.getUserName())
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onCallback.onCallback(true);
                        }else{
                            onCallback.onCallback(false);
                        }
                    }
                });
    }
    
}
