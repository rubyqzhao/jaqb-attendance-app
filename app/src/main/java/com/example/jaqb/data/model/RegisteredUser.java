package com.example.jaqb.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

@IgnoreExtraProperties
public class RegisteredUser {

    private String uID;
    private String fName;
    private String lName;
    private UserLevel level;

    public RegisteredUser() {}

    public RegisteredUser(String uID, String fName, String lName)
    {
        this(uID, UserLevel.STUDENT, fName, lName);
    }

    public RegisteredUser(String uID, UserLevel level, String fName, String lName)
    {
        this.uID = uID;
        this.level = level;
        this.fName = fName;
        this.lName = lName;
    }

    public String getuID() {
        return uID;
    }

    public UserLevel getLevel()
    {
        return level;
    }

    public String getfName()
    {
        return fName;
    }

    public String getlName()
    {
        return lName;
    }
}
