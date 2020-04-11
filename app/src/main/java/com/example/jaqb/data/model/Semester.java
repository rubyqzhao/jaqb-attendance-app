package com.example.jaqb.data.model;

import android.util.Log;

import java.util.TimeZone;

/**
 * This class contains the attributes of for a given semester
 *
 * @author  Joshua Drumm
 * @version 2.0
 * @since   2020-4-10
 */

public class Semester {

    private TimeZone timeZone;
    private SemesterDate startSemesterDate, endSemesterDate;
    private SemesterDate[] offDays;

    public Semester(String tz, SemesterDate startSemesterDate, SemesterDate endSemesterDate, SemesterDate[] offDays){
        timeZone = TimeZone.getTimeZone(tz);
        this.startSemesterDate = startSemesterDate;
        this.endSemesterDate = endSemesterDate;
        this.offDays = offDays;
    }

    /**
     * Gets the timeZoneID for the current semester
     * @return The semester's timeZoneID
     */
    public String getTimeZoneID()
    {
        return timeZone.getID();
    }

    /**
     * Gets the start date of the semester
     * @return The start date of the semester as a SemesterDate
     */
    public SemesterDate getStartSemesterDate()
    {
        return startSemesterDate;
    }

    /**
     * Gets the end date of the semester
     * @return The end date of the semester as a SemesterDate
     */
    public SemesterDate getEndSemesterDate()
    {
        return endSemesterDate;
    }

    /**
     * Gets the semester's off days formatted as needed for the Calender interfaces
     * @return The String of off days formatted
     */
    public String getOffDaysFormatted()
    {
        if(offDays.length == 0)
            return "";
        String days = offDays[0].toString();
        for(int i = 1; i < offDays.length; i++)
            days += "," + offDays[i];
        Log.i("DAYS", days);
        return days;
    }

    /**
     * Gets the off days of the semester
     * @return A list of the semester's off days
     */
    public SemesterDate[] getOffDays()
    {
        return offDays;
    }
}
