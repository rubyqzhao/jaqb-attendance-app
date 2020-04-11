package com.example.jaqb.data.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.jaqb.services.CalendarServices;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class contains the attributes for the logged-in user
 *
 * @author  Joshua Drumm
 * @version 2.0
 * @since   2020-4-10
 */

public class LoggedInUser extends Observable {
    private FirebaseUser firebaseUser;
    private RegisteredUser registeredUser;
    private List<Course> registeredCourses = new ArrayList<>();
    private List<String> courseNames = new ArrayList<>();
    private Semester semester;
    private List<Badge> badgeList = new ArrayList<>();

    public LoggedInUser(FirebaseUser firebaseUser, RegisteredUser registeredUser)
    {
        this.firebaseUser = firebaseUser;
        this.registeredUser = registeredUser;
        this.courseNames.addAll(registeredUser.getCourses());
    }

    /**
     * Adds a course to the user's list of registered courses
     * @param course The course to add
     */
    public void updateCourse(Course course){
        this.courseNames.add(course.getCode());
        this.registeredCourses.add(course);
    }

    /**
     * Gets the user's list of registered courses
     * @return The list of registered courses for the user
     */
    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    /**
     * Sets the list of registered courses
     * @param registeredCourses The user's list of courses
     */
    public void setRegisteredCourses(List<Course> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

    /**
     * Gets the user's badges
     * @return The list of badges the user has earned
     */
    public List<Badge> getBadges() {
        return badgeList;
    }

    /**
     * Sets the list of badges the user has earned
     * @param badgeList The list of badges
     */
    public void setBadges(List<Badge> badgeList) {
        this.badgeList = badgeList;
    }

    /**
     * Gets the user's display name, being their first and last name
     * @return The user's display name
     */
    public String getDisplayName()
    {
        return getfName() + " " + getlName();
    }

    /**
     * Gets the user's first name
     * @return The user's first name
     */
    public String getfName()
    {
        return registeredUser.getfName();
    }

    /**
     * Gets the user's last name
     * @return The user's last name
     */
    public String getlName()
    {
        return registeredUser.getlName();
    }

    /**
     * Gets the user's level, coming from the UserLevel enum
     * @return The user's authentication level
     */
    public UserLevel getLevel()
    {
        return registeredUser.getLevel();
    }

    /**
     * Gets the firebase user's unique ID. This is Unique in FireBaseAuth
     * @return The user's unique ID
     */
    public String getuID()
    {
        return firebaseUser.getUid();
    }

    /**
     * Gets a list of the user's course names
     * @return The list of course names
     */
    public List<String> getCourseNames() {
        return courseNames;
    }

    /**
     * Sets the semester for the user
     * @param semester The user's semester
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    /**
     * Gets the user's current semester
     * @return The user's semester
     */
    public Semester getSemester()
    {
        return semester;
    }

    /**
     * Gets the user's next available course to check-in to
     * @return The user's next course
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Course getNextCourse()
    {
        return CalendarServices.getNextCourse(registeredCourses, this);
    }
}
