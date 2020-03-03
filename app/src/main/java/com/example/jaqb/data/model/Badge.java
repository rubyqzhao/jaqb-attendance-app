package com.example.jaqb.data.model;

/**
 * @author rubyqzhao
 *
 * Contains the attributes of a student achievement badge icon
 * */
public class Badge {
    private int badgeID;
    private String badgeName;
    private int imageResource;

    public Badge(int id, String name, int url) {
        badgeID = id;
        badgeName = name;
        imageResource = url;
    }

    public void setID(int id) {
        badgeID = id;
    }

    public int getID() {

        return badgeID;
    }

    public void setName(String name) {
        badgeName = name;
    }

    public String getName() {
        return badgeName;
    }

    public void setImage(int url) {
        imageResource = url;
    }

    public int getImage() {
        return imageResource;
    }
}
