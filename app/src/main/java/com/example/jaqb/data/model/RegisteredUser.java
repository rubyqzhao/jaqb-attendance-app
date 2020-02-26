package com.example.jaqb.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class RegisteredUser {

    private String uID;
    private UserLevel level;

    public RegisteredUser(String uID)
    {
        this(uID, UserLevel.STUDENT);
    }

    public RegisteredUser(String uID, UserLevel level)
    {
        this.uID = uID;
        this.level = level;
    }

    public String getUID() {
        return uID;
    }

    public UserLevel getLevel()
    {
        return level;
    }
}
