package com.example.jaqb.data.model;

/**
 * Contains the attributes of a student achievement badge icon
 *
 * @author  rubyqzhao
 * @version 1.0
 * @since   2020-4-10
 */

public class Badge {
    private int badgeID;
    private String badgeName;
    private int imageResource;

    public Badge(int id, String name, int url) {
        badgeID = id;
        badgeName = name;
        imageResource = url;
    }

    /**
     * Sets the ID of the badge
     * @param id The Badge Id
     */
    public void setID(int id) {
        badgeID = id;
    }

    /**
     * Gets the ID of the badge
     * @return The Badge Id
     */
    public int getID() {

        return badgeID;
    }

    /**
     * Sets the name of the badge
     * @param name The Badge Name
     */
    public void setName(String name) {
        badgeName = name;
    }

    /**
     * Gets the name of the badge
     * @return The Badge Name
     */
    public String getName() {
        return badgeName;
    }

    /**
     * Sets the image of the badge
     * @param url The Badge image resource
     */
    public void setImage(int url) {
        imageResource = url;
    }

    /**
     * Gats the image of the badge
     * @return The Badge image resource
     */
    public int getImage() {
        return imageResource;
    }
}
