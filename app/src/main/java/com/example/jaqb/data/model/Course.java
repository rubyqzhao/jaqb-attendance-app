package com.example.jaqb.data.model;

import java.time.LocalTime;

/**
 * @author amanjotsingh
 *
 * This class contains the attributes of the courses that the students can use to
 * register for a class.
 * */

public class Course {

    public Course(){
    }

    private String courseName;
    private String code;
    private String instructorName;
    private String location;
    private LocalTime classStartTime;
    private LocalTime classEndTime;
    private String days;

    public LocalTime getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(LocalTime classStartTime) {
        this.classStartTime = classStartTime;
    }

    public LocalTime getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(LocalTime classEndTime) {
        this.classEndTime = classEndTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
