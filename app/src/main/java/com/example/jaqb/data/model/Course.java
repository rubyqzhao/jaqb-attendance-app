package com.example.jaqb.data.model;

/**
 * This class contains the attributes of the courses that the students can use to
 * register for a class.
 *
 * @author amanjotsingh
 * @version 1.0
 * @since   2020-4-10
 */

public class Course {

    private String code;
    private String courseName;
    private String instructorName;
    private String time;
    private String days;
    private String courseQRCode;
    private double longitude;
    private double latitude;

    public Course(){
    }

    public Course(String code, String courseName) {
        this.code = code;
        this.courseName = courseName;
    }

    /**
     * Gets the course code
     * @return The course code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the course code
     * @param code The course code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the course code
     * @return The course code
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name
     * @return The course code
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets the course code
     * @return The course code
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Sets the instructor name
     * @param instructorName The instructor's name
     */
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    /**
     * Gets the days of the course
     * @return The days of the course
     */
    public String getDays() {
        return days;
    }

    /**
     * Sets the course's days
     * Formatted as "MO,TU,WE,TH,FR,SA,SU"
     * @param days The course's days
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * Gets the time of the course
     * @return The time of the course
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the course's time
     * @param time The course time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the longitude of the course
     * @return The course longitude
     */
    public double getLongitude() { return longitude; }

    /**
     * Sets the course's longitude
     * @param longitude The longitude
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Gets the latitude of the course
     * @return The latitude of the course
     */
    public double getLatitude() { return latitude; }

    /**
     * Sets the course's latitude
     * @param latitude The latitude
     */
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**
     * Gets the QR code of the course
     * @return The QR code information as a string
     */
    public String getCourseQRCode() { return courseQRCode; }

    /**
     * Sets the course's QR Code string
     * @param courseQRCode The String representing the QR Code's information
     */
    public void setCourseQRCode(String courseQRCode) { this.courseQRCode = courseQRCode; }
}
