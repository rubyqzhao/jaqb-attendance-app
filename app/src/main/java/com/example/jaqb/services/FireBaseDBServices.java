package com.example.jaqb.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jaqb.data.model.RegisteredUser;
import com.example.jaqb.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

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
    private FirebaseUser currentUser;
    private FirebaseDatabase database;

    public FireBaseDBServices()
    {
        mAuth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();
    }

    public static FireBaseDBServices getInstance()
    {
        return dbService;
    }

    public boolean registerUser(User newUser){
        mAuth.createUserWithEmailAndPassword(newUser.getUserName(), newUser.getPassword())
            .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                        RegisteredUser registeredUser = new RegisteredUser(user.getUid());
                        DatabaseReference reff = database.getReference().child("User").push();
                        reff.setValue(registeredUser, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            }
                        });
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

    public  boolean loginUser(String email, String password)
    {
        final boolean[] successful = {false};
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        currentUser = mAuth.getCurrentUser();
                        //updateUI(user);
                        successful[0] = true;
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        //updateUI(null);
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
        return successful[0];
    }

    public FirebaseUser getCurrentUser()
    {
        return currentUser;
    }
}
