package com.cmput301.w19t06.theundesirablejackals.Database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.LoginActivity;
import com.cmput301.w19t06.theundesirablejackals.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;

public class DatabaseHelper {
    // database path to users
    private final static String PATH_USERS = "users";
    private static volatile DatabaseHelper dbinstance;
    private final static String INFO_DUMP_FLAG = "INFO_DUMP";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Context context;
    private Gson gson;

    private DatabaseHelper() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.gson = new Gson();
    }

    private void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get the same instance of database helper, This is done to only have one DatabaseHelper running
     * @param context the running context that called for getInstance
     * @return the only instance of DatabaseHelper
     */
    public static DatabaseHelper getInstance(Context context) {
        if(dbinstance == null) {
            synchronized (DatabaseHelper.class) {
                if (dbinstance == null) {
                    dbinstance = new DatabaseHelper();
                }
            }
        }
        dbinstance.setContext(context);
        return  dbinstance;
    }


    /**
     * Creates a new user account which registers a new authentication to FireBase and creates
     * a JSON format of the newly created User object to Firebase database.
     * @param username  the new user name of the account
     * @param email     new email (must be unique) to be used for logging in
     * @param password  password that will be used by user for authentication
     * @param phonenumber  contact phone number
     */
    public void createAccount(final String username, final String email, final String password, final String phonenumber) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = database.getReference(PATH_USERS).child(userid);


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(username, gson.toJson(new User(username, email, phonenumber)));

                            // save class user to database
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(context, MainHomeViewActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(context,
                                    "You can't register with this email and or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Authenticates a user using email and password.
     * @param email     unique user email
     * @param password  password.... just a password
     */
    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(context, MainHomeViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Get the User object of the current logged in user (on the running device)
     * @return User object of the logged in user
     */
    public void getLoggedinUser() {

        if (isUserLoggedin()) {
            Log.i(INFO_DUMP_FLAG, "Starting extraction");
            String userid = firebaseUser.getUid();

            databaseReference = database.getReference(PATH_USERS);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userdata = dataSnapshot.getValue(User.class);
                    Log.i(INFO_DUMP_FLAG, userdata.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i(INFO_DUMP_FLAG, "ERROR HAPPENED");
                }
            });
        }
    }



    public boolean isUserLoggedin() {
        return firebaseUser != null;
    }

    public void signOut() {
        firebaseAuth.signOut();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

}
