package com.example.jaqb.data.model;

import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;

public class LoggedInUser extends Observable {
    private FirebaseUser firebaseUser;
    private RegisteredUser registeredUser;

    public LoggedInUser(FirebaseUser firebaseUser, RegisteredUser registeredUser)
    {
        this.firebaseUser = firebaseUser;
        this.registeredUser = registeredUser;
    }

    public String getDisplayName()
    {
        return getfName() + " " + getlName();
    }

    public String getfName()
    {
        return registeredUser.getfName();
    }

    public String getlName()
    {
        return registeredUser.getlName();
    }

    public UserLevel getLevel()
    {
        return registeredUser.getLevel();
    }

    public String getuID()
    {
        return firebaseUser.getUid();
    }

    //List of courses
}
