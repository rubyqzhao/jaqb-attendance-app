package com.example.jaqb.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

@IgnoreExtraProperties
public class RegisteredUser {

    private String fName;
    private String lName;
    private UserLevel level;
    private List<String> courses;

    public RegisteredUser(String fName, String lName)
    {
        this(UserLevel.STUDENT, fName, lName);
    }

    public RegisteredUser(UserLevel level, String fName, String lName)
    {
        this.level = level;
        this.fName = fName;
        this.lName = lName;
    }

    public RegisteredUser(String fName, String lName, UserLevel level, List<String> courses) {
        this.fName = fName;
        this.lName = lName;
        this.level = level;
        this.courses = courses;
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

    public List<String> getCourses() {
        return courses;
    }
}
