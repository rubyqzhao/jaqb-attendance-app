package com.example.jaqb.data.model;

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

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

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
