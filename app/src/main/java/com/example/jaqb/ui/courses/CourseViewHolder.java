package com.example.jaqb.ui.courses;

import android.widget.Button;
import android.widget.TextView;

public class CourseViewHolder {
    private TextView courseCode;
    private TextView courseName;
    private TextView courseDays;
    private TextView courseInstructor;
    private TextView courseTime;

    public TextView getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(TextView courseTime) {
        this.courseTime = courseTime;
    }

    public TextView getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(TextView courseCode) {
        this.courseCode = courseCode;
    }

    public TextView getCourseName() {
        return courseName;
    }

    public void setCourseName(TextView courseName) {
        this.courseName = courseName;
    }

    public TextView getCourseDays() {
        return courseDays;
    }

    public void setCourseDays(TextView courseDays) {
        this.courseDays = courseDays;
    }

    public TextView getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(TextView courseInstructor) {
        this.courseInstructor = courseInstructor;
    }
}