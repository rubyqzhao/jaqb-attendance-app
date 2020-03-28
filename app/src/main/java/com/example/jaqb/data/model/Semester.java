package com.example.jaqb.data.model;

import android.util.Log;

import java.util.TimeZone;

/**
 * @author jkdrumm
 *
 * This class contains the attributes of for a given semester
 * */

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

    public String getTimeZoneID()
    {
        return timeZone.getID();
    }

    public SemesterDate getStartSemesterDate()
    {
        return startSemesterDate;
    }

    public SemesterDate getEndSemesterDate()
    {
        return endSemesterDate;
    }

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

    public SemesterDate[] getDays()
    {
        return offDays;
    }
}
