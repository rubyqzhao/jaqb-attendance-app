package com.example.jaqb.data.model;

/**
 * This class contains the attributes of a user entered at the time for registering for the app.
 *
 * @author amanjotsingh
 * @version 2.0
 * @since   2020-4-10
 */

public class User {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;

    public User(){
    }

    /**
     * Gets the user's username
     * @return The user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's userName
     * @param userName The user's userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the user's password
     * @return The user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password The user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's first name
     * @return The user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name
     * @param firstName The user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name
     * @return The user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name
     * @param lastName The user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
