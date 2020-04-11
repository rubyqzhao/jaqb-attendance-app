package com.example.jaqb.data.model;

/**
 * This class will hold information for a semester including its year, month, day,
 * And the string formatted needed for Calendar and Time purposes
 *
 * @author  Joshua Drumm
 * @version 2.0
 * @since   2020-4-10
 */

public class SemesterDate
{
    private int year, month, day;
    private String formattedString;

    public SemesterDate(String date)
    {
        String dateInfo[] = date.split("[-/]");
        year = Integer.parseInt(dateInfo[2]);
        month = Integer.parseInt(dateInfo[0]);
        day = Integer.parseInt(dateInfo[1]);
        formattedString = year + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "") + day + "T080000Z";
    }

    /**
     * Gets the year the semester date is in
     * @return The semester's year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Gets the month the semester date is in
     * @return The semester's month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Gets the day the semester date is in
     * @return The semester's day
     */
    public int getDay()
    {
        return day;
    }

    @Override
    public String toString()
    {
        return formattedString;
    }
}
