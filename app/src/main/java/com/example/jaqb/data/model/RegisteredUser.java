package com.example.jaqb.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 *
 * @author  Joshua Drumm, amanjotsingh
 * @version 2.0
 * @since   2020-4-10
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

    /**
     * Gets the user's level, coming from the UserLevel enum
     * @return The user's authentication level
     */
    public UserLevel getLevel()
    {
        return level;
    }

    /**
     * Gets the user's first name
     * @return The user's first name
     */
    public String getfName()
    {
        return fName;
    }

    /**
     * Gets the user's last name
     * @return The user's last name
     */
    public String getlName()
    {
        return lName;
    }

    /**
     * Gets the user's list of courses
     * @return The list of courses for the user
     */
    public List<String> getCourses() {
        return courses;
    }
}
