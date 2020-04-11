package com.example.jaqb.ui.courses;

import android.widget.Button;
import android.widget.TextView;

/**
 * Holder object for the courses view being displayed in the courses adapter.
 * Used to set values of the course list in the user's courses page.
 *
 * @author Amanjot Singh
 * @version 1.0
 *
 */
public class CourseViewHolder {
    private TextView courseCode;
    private TextView courseName;
    private TextView courseDays;
    private TextView courseInstructor;
    private TextView courseTime;

    /**
     * Returns the textview object representation for course time.
     * @return TextView courseTime
     */
    public TextView getCourseTime() {
        return courseTime;
    }

    /**
     * Sets the textview object representation for course time.
     * @param courseTime    the view object for course time
     */
    public void setCourseTime(TextView courseTime) {
        this.courseTime = courseTime;
    }

    /**
     * Returns the textview object representation for course code.
     * @return TextView courseCode
     */
    public TextView getCourseCode() {
        return courseCode;
    }

    /**
     * Sets the textview object representation for course code.
     * @param courseCode    the view object for course code
     */
    public void setCourseCode(TextView courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Returns the textview object representation for course name.
     * @return  textView courseName
     */
    public TextView getCourseName() {
        return courseName;
    }

    /**
     * Sets the textview object representation for course name.
     * @param courseName    the view object for course name
     */
    public void setCourseName(TextView courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the textview object representation for course days.
     * @return TextView courseDays
     */
    public TextView getCourseDays() {
        return courseDays;
    }

    /**
     * Sets the textview object representation for course days.
     * @param courseDays    the view object for course days
     */
    public void setCourseDays(TextView courseDays) {
        this.courseDays = courseDays;
    }

    /**
     * Returns the textview object representation for course instructor.
     * @return TextView courseInstructor
     */
    public TextView getCourseInstructor() {
        return courseInstructor;
    }

    /**
     * Sets the textview object representation for course instructor.
     * @param courseInstructor  the view object for course instructor
     */
    public void setCourseInstructor(TextView courseInstructor) {
        this.courseInstructor = courseInstructor;
    }
}