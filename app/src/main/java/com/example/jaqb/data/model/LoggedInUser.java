package com.example.jaqb.data.model;

import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class contains the attributes for the logged-in user
 * */

public class LoggedInUser extends Observable {
    private FirebaseUser firebaseUser;
    private RegisteredUser registeredUser;
    private List<Course> registeredCourses = new ArrayList<>();
    private List<String> courseNames = new ArrayList<>();
    private Semester semester;

    public void updateCourse(Course course){
        this.courseNames.add(course.getCode());
        this.registeredCourses.add(course);
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void setRegisteredCourses(List<Course> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

    public LoggedInUser(FirebaseUser firebaseUser, RegisteredUser registeredUser)
    {
        this.firebaseUser = firebaseUser;
        this.registeredUser = registeredUser;
        this.courseNames.addAll(registeredUser.getCourses());
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

    public List<String> getCourseNames() {
        return courseNames;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Semester getSemester()
    {
        return semester;
    }
}
