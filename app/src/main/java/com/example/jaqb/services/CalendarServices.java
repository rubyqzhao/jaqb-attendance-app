package com.example.jaqb.services;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.data.model.SemesterDate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class CalendarServices
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getNextDaySemester(String courseDays, LoggedInUser currentUser)
    {
        String[] days = courseDays.split("[,]");
        long soonestDay = getTimeOfNextDayCodeSemester(days[0], currentUser);
        for(int i = 1; i < days.length; i++)
        {
            long nextDay = getTimeOfNextDayCodeSemester(days[i], currentUser);
            if(nextDay < soonestDay)
                soonestDay = nextDay;
        }
        return soonestDay;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getNextTime(Course course, String timeZone)
    {
        String[] days = course.getDays().split("[,]");
        ZoneId zone = ZoneId.of(timeZone);
        long soonestDay = getTimeOfNextDayCode(days[0], zone);
        for(int i = 1; i < days.length; i++)
        {
            long nextDay = getTimeOfNextDayCode(days[i], zone);
            if(nextDay < soonestDay)
                soonestDay = nextDay;
        }
        String timeSplit[] = course.getTime().split("[:]");
        if(timeSplit.length != 2)
            return -1;

        int hour, minute;
        try {
            hour = Integer.parseInt(timeSplit[0]);
            minute = Integer.parseInt(timeSplit[1]);
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
        return soonestDay + (hour * 60 + minute) * 60000;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Course getNextCourse(List<Course> courses, LoggedInUser user)
    {
        long currentTime = getCurrentTime(ZoneId.of(user.getSemester().getTimeZoneID()));
        Course soonestCourse = courses.get(0);
        long soonestCourseTime = CalendarServices.getNextTime(courses.get(0), user.getSemester().getTimeZoneID());
        for(int i = 1; i < courses.size(); i++)
        {
            Course course = courses.get(i);
            long courseTime = CalendarServices.getNextTime(courses.get(i), user.getSemester().getTimeZoneID());
            if(courseTime < soonestCourseTime && courseTime > currentTime)
            {
                soonestCourseTime = courseTime;
                soonestCourse = course;
            }
        }
        return soonestCourse;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DayOfWeek getDayOfWeek(String dayCode)
    {
        DayOfWeek dayOfWeek;
        switch(dayCode)
        {
            case "MO":
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "TU":
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "WE":
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "TH":
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "FR":
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "SA":
                dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "SU":
                dayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                dayOfWeek = null;
        }
        return dayOfWeek;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTimeOfNextDayCodeSemester(String dayCode, LoggedInUser currentUser)
    {
        DayOfWeek dayOfWeek = getDayOfWeek(dayCode);
        SemesterDate semesterDate = currentUser.getSemester().getStartSemesterDate();
        ZoneId zone = ZoneId.of(currentUser.getSemester().getTimeZoneID());
        return ZonedDateTime.of(semesterDate.getYear(), semesterDate.getMonth(), semesterDate.getDay(), 0, 0, 0, 0, zone).toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek)).atStartOfDay().toInstant(zone.getRules().getOffset(LocalDateTime.now())).getEpochSecond() * 1000;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTimeOfNextDayCode(String dayCode, ZoneId zone)
    {
        DayOfWeek dayOfWeek = getDayOfWeek(dayCode);
        return ZonedDateTime.now(zone).toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek)).atStartOfDay().toInstant(zone.getRules().getOffset(LocalDateTime.now())).getEpochSecond() * 1000;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getCurrentTime(ZoneId zone)
    {
        return ZonedDateTime.now(zone).toInstant().getEpochSecond() * 1000;
    }

    public static String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") :
                        Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }
}
