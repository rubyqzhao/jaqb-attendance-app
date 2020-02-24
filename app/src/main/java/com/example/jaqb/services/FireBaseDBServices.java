package com.example.jaqb.services;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amanjotsingh
 *
 * This class contains the methods required to communicate with the database
 * */

public class FireBaseDBServices{

    private FirebaseDatabase fDB;

    public FireBaseDBServices() {
        this.fDB = FirebaseDatabase.getInstance();
    }

    public int registerUser(User newUser){
        DatabaseReference reff;
        try{
            reff = fDB.getReference().child("User");
            reff.push().setValue(newUser);
            return 1;
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
