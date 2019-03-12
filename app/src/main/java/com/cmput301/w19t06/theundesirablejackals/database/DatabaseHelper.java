package com.cmput301.w19t06.theundesirablejackals.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
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
    private final static String PATH_BOOKS = "books";
    private final static String PATH_REGISTERED = "registered";
    private final static String PATH_FAVOURITE = "favourites";


    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference;
    private DatabaseReference booksReference;
    private DatabaseReference registeredReference;
    private DatabaseReference favouriteReference;
    private FirebaseUser currentUser;




//    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   //


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

    }



    /**
     * Get the same instance of database helper, This is done to only have one DatabaseHelper running
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


    /**
     * Get the FirebaseAuth user object that is currently in use
     * @return FirebaseAuth user object
     */
    public FirebaseUser getCurrentUser(){return currentUser;}


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
     * Goes to the firebase database and fetches (asynchronously) the currentUser custom User object
     * @param  onCallback  The callback which is passed in, to be called upon successful data acquisition
     *                     used to pass data back to calling activity/fragment/class
     */
    public void getUserFromDatabase(final UserCallback onCallback){
        usersReference
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d(TAG, user.toString());
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
     * Goes to the firebase database and saves (asynchronously) the currentUser's custom User object
     * @param  user The currentuser's custom User object that is to be written/updated in the database
     * @param  onCallback  The callback which is passed in, to be called upon successful data write
     *                     used to pass completion status back to calling activity/fragment/class
     */
    private void saveCurrentUser(User user, final BooleanCallback onCallback){
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




//    public void createAccount(final String username, String password, final String email, final String phonenumber) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//                            assert firebaseUser != null;
//                            String userid = firebaseUser.getUid();
//
//                            usersReference = FirebaseDatabase.getInstance().getReference(PATH_USERS).child(userid);
//
//                            HashMap<String, User> hashMap = new HashMap<>();
//                            hashMap.put(username, new User(username, email, phonenumber));
//
//                            // save class user to database
//                            usersReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(context, MainHomeViewActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(intent);
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(context,
//                                    "You can't register with this email and or password", Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
//    }



//    public void login(String email, String password) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Intent intent = new Intent(context, MainHomeViewActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
//    }


//    /**
//     * Get the same instance of database helper, This is done to only have one DatabaseHelper running
//     * @param context the running context that called for getInstance
//     * @return the only instance of DatabaseHelper
//     */
//    public static DatabaseHelper getInstance(Context context) {
//        if(dbinstance == null) {
//            synchronized (DatabaseHelper.class) {
//                if (dbinstance == null) {
//                    dbinstance = new DatabaseHelper();
//                }
//            }
//        }
//        dbinstance.setContext(context);
//        return  dbinstance;
//    }


//    /**
//     * Creates a new user account which registers a new authentication to FireBase and creates
//     * a JSON format of the newly created User object to Firebase database.
//     * @param username  the new user name of the account
//     * @param email     new email (must be unique) to be used for logging in
//     * @param password  password that will be used by user for authentication
//     * @param phonenumber  contact phone number
//     */
//    public void createAccount(final String username, final String email, final String password, final String phonenumber) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            firebaseUser = firebaseAuth.getCurrentUser();
//
//                            assert firebaseUser != null;
//                            String userid = firebaseUser.getUid();
//
//                            databaseReference = database.getReference(PATH_USERS).child(userid);
//
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put(username, gson.toJson(new User(username, email, phonenumber)));
//
//                            // save class user to database
//                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(context, MainHomeViewActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(intent);
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(context,
//                                    "You can't register with this email and or password", Toast.LENGTH_SHORT).show();


//    /**
//     * Authenticates a user using email and password.
//     * @param email     unique user email
//     * @param password  password.... just a password
//     */
//    public void login(String email, String password) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Intent intent = new Intent(context, MainHomeViewActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                        }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }


//    /**
//     * Get the User object of the current logged in user (on the running device)
//     * @return User object of the logged in user
//     */
//    public void getLoggedinUser() {
//
//        if (isUserLoggedin()) {
//            Log.i(INFO_DUMP_FLAG, "Starting extraction");
//            String userid = currentUser.getUid();
//
//
//
//            usersReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    User userdata = dataSnapshot.getValue(User.class);
//                    Log.i(INFO_DUMP_FLAG, userdata.toString());
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.i(INFO_DUMP_FLAG, "ERROR HAPPENED");
//                }
//            });
//        }
//    }

}
