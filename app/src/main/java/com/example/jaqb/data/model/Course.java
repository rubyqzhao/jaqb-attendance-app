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

    public Course(String courseName, String code, String days, String instructorName) {
        this.courseName = courseName;
        this.code = code;
        this.instructorName = instructorName;
        this.days = days;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    private String code;
    private String courseName;
    private String instructorName;
    //private String location;
    //private LocalTime classStartTime;
    //private LocalTime classEndTime;
    private String days;
}
