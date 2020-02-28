package com.example.jaqb.data.model;

/**
 * @author amanjotsingh
 *
 * This class contains the attributes of the courses that the students can use to
 * register for a class.
 * */

public class Course {

    private String code;
    private String courseName;
    private String instructorName;
    private String time;
    private String days;

    public Course(){
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
