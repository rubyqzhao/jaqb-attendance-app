package com.example.jaqb.services;

import com.example.jaqb.data.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
