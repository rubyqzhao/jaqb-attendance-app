package com.example.jaqb.data.model;

import java.util.TimeZone;

/**
 * @author jkdrumm
 *
 * This class contains the attributes of for a given semester
 * */

public class Semester {

    private TimeZone timeZone;
    private String startDate, endDate;

    public Semester(String tz, String startDate, String endDate){
        timeZone = TimeZone.getTimeZone(tz);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTimeZoneID()
    {
        return timeZone.getID();
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }
}
