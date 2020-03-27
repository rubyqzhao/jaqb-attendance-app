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
    private Date startDate, endDate;
    private Date[] offDays;

    public Semester(String tz, Date startDate, Date endDate, Date[] offDays){
        timeZone = TimeZone.getTimeZone(tz);
        this.startDate = startDate;
        this.endDate = endDate;
        this.offDays = offDays;
    }

    public String getTimeZoneID()
    {
        return timeZone.getID();
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
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

    public Date[] getDays()
    {
        return offDays;
    }
}
