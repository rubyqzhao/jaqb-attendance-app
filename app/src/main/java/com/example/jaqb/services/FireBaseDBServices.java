package com.example.jaqb.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jaqb.CheckInActivity;
import com.example.jaqb.MainActivity;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.data.model.RegisteredUser;
import com.example.jaqb.data.model.User;
import com.example.jaqb.data.model.UserLevel;
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

import java.util.Observer;

/**
 * @author amanjotsinghs
 * @author jkdrumm
 *
 * This class contains the methods required to communicate with the database
 * */

public class FireBaseDBServices {

    private static final String TAG = "EmailPassword";
    private static final FireBaseDBServices dbService = new FireBaseDBServices();

    private FirebaseAuth mAuth;
    private LoggedInUser currentUser;
    private FirebaseDatabase database;

    private FireBaseDBServices()
    {
        mAuth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();
    }

    public static FireBaseDBServices getInstance()
    {
        return dbService;
    }

    public boolean registerUser(final User newUser, final Context context){
        mAuth.createUserWithEmailAndPassword(newUser.getUserName(), newUser.getPassword())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        RegisteredUser registeredUser =
                                new RegisteredUser(newUser.getFirstName(), newUser.getLastName());
                        DatabaseReference reff = database.getReference("User").child(firebaseUser.getUid());
                        reff.child("fname").setValue(registeredUser.getfName());
                        reff.child("lname").setValue(registeredUser.getlName());
                        reff.child("level").setValue(registeredUser.getLevel());
                        //Go to login page
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        //updateUI(null);
                    }

                    // [START_EXCLUDE]
                    //hideProgressBar();
                    // [END_EXCLUDE]
                }
            });
        return true;
    }

    public  boolean loginUser(String email, String password, final Context context, final Observer observer)
    {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        database.getReference("User").child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String fname = (String) dataSnapshot.child("fname").getValue();
                                    String lname = (String) dataSnapshot.child("lname").getValue();
                                    UserLevel level = UserLevel.valueOf((String) dataSnapshot.child("level").getValue());
                                    RegisteredUser registeredUser = new RegisteredUser(level, fname, lname);
                                    currentUser = new LoggedInUser(firebaseUser, registeredUser);
                                    observer.update(currentUser, currentUser.getLevel());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    observer.update(null, null);
                                }
                            });
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        observer.update(null, null);
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful()) {
                        //mStatusTextView.setText(R.string.auth_failed);
                    }
                    //hideProgressBar();
                    // [END_EXCLUDE]
                }
            });
        // [END sign_in_with_email]
        return true;
    }

    public LoggedInUser getCurrentUser()
    {
        return currentUser;
    }

    public void seeIfStillLoggedIn(final Context context)
    {
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            System.out.println("Logged-in");
            database.getReference("User").child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fname = (String) dataSnapshot.child("fname").getValue();
                            String lname = (String) dataSnapshot.child("lname").getValue();
                            UserLevel level = UserLevel.valueOf((String) dataSnapshot.child("level").getValue());
                            RegisteredUser registeredUser = new RegisteredUser(level, fname, lname);
                            currentUser = new LoggedInUser(firebaseUser, registeredUser);
                            System.out.println(currentUser.getDisplayName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        else
            System.out.println("Not Logged-in");
        //goToUserHomepage(context);
    }
}
