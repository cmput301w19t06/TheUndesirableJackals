package com.cmput301.w19t06.theundesirablejackals.Database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.LoginActivity;
import com.cmput301.w19t06.theundesirablejackals.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    // database path to users
    private final static String PATH_USERS = "users";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Context context;
    private Gson gson;

    private DatabaseHelper(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.context = context;
        this.gson = new Gson();
    }

    public static DatabaseHelper getInstance(Context context) {
        return new DatabaseHelper(context);
    }

    public void createAccount(final String username, final String email, final String password, final String phonenumber) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = database.getReference(PATH_USERS);

                            Map<String, User> user = new HashMap<>();
                            user.put(username, new User(username, email, phonenumber));

                            // save class user to database
                            databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

}
