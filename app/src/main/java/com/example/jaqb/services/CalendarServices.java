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

/**
 * A class to handle services and functions relating to time and the calendar
 *
 * @author Joshua Drumm
 * @version 1.0
 * @since   2020-4-10
 */
public class CalendarServices
{
    /**
     * Gets the next day happening in the semester, starting from the first day in the semester
     * @param courseDays The days the course happens on
     * @param currentUser The current logged-in user
     * @return The time in milliseconds since the Unix epoch that the day begins on
     */
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

    /**
     * Gets the next day and time happening for a course in a specific timezone
     * @param course The course to get the next day for
     * @param timeZone The timezone to use
     * @return The time in milliseconds since the Unix epoch that the day begins on
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getNextTime(Course course, String timeZone)
    {
        String[] days = course.getDays().split("[,]");
        ZoneId zone = ZoneId.of(timeZone);
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
        long soonestDay = getTimeOfNextDayCode(days[0], zone, hour, minute);
        for(int i = 1; i < days.length; i++)
        {
            long nextDay = getTimeOfNextDayCode(days[i], zone, hour, minute);
            if(nextDay < soonestDay)
                soonestDay = nextDay;
        }

        return soonestDay + (hour * 60 + minute) * 60000;
    }

    /**
     * Gets the user's next available course to check-in to
     * @param courses The list of courses the user is registered in
     * @param user The user whose courses to check
     * @return The user's next course
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Course getNextCourse(List<Course> courses, LoggedInUser user)
    {
        long currentTime = getCurrentTime(ZoneId.of(user.getSemester().getTimeZoneID()));
        Course soonestCourse = courses.get(0);
        long soonestCourseTime = getNextTime(courses.get(0), user.getSemester().getTimeZoneID());
        for(int i = 1; i < courses.size(); i++)
        {
            Course course = courses.get(i);
            long courseTime = getNextTime(courses.get(i), user.getSemester().getTimeZoneID());
            if(courseTime < soonestCourseTime && courseTime > currentTime)
            {
                soonestCourseTime = courseTime;
                soonestCourse = course;
            }
        }
        return soonestCourse;
    }

    /**
     * Gets the day of the week based on the formatted day code
     * MO,TU,WE,TH,FR,SA,SU -> DayOfWeek Enum
     * @param dayCode The string representing the day
     * @return The DayOfWeek enum corresponding to the string
     */
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

    /**
     * Gets the first time of a course happening on a specific day in the semester
     * @param dayCode The string code that the course happens next on
     * @param currentUser The user currently logged in
     * @return The Unix Epoch time in milliseconds for the time of the start of the day
     *         for the course's first occurrence on the specified day in the semester
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTimeOfNextDayCodeSemester(String dayCode, LoggedInUser currentUser)
    {
        DayOfWeek dayOfWeek = getDayOfWeek(dayCode);
        SemesterDate semesterDate = currentUser.getSemester().getStartSemesterDate();
        ZoneId zone = ZoneId.of(currentUser.getSemester().getTimeZoneID());
        return ZonedDateTime.of(semesterDate.getYear(), semesterDate.getMonth(), semesterDate.getDay(), 0, 0, 0, 0, zone).toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek)).atStartOfDay().toInstant(zone.getRules().getOffset(LocalDateTime.now())).getEpochSecond() * 1000;
    }

    /**
     * Gets the next time of a course happening on a specific day in the semester
     * @param dayCode The string code that the course happens next on
     * @param zone The timezone to compare to. This is usually the user's timezone
     * @return The Unix Epoch time in milliseconds for the time of the start of the day
     *         for the course's next occurrence on the specified day in the semester
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTimeOfNextDayCode(String dayCode, ZoneId zone)
    {
        DayOfWeek dayOfWeek = getDayOfWeek(dayCode);
        return ZonedDateTime.now(zone).toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek)).atStartOfDay().toInstant(zone.getRules().getOffset(LocalDateTime.now())).getEpochSecond() * 1000;
    }

    /**
     * Gets the next time of a course happening on a specific day in the semester with a given time
     * @param dayCode The string code that the course happens next on
     * @param zone The timezone to compare to. This is usually the user's timezone
     * @param hour The hour the class starts
     * @param minute The minute the class starts
     * @return The Unix Epoch time in milliseconds for the time of the start of the day
     *         for the course's next occurrence on the specified day in the semester
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTimeOfNextDayCode(String dayCode, ZoneId zone, int hour, int minute)
    {
        DayOfWeek dayOfWeek = getDayOfWeek(dayCode);
        return ZonedDateTime.now(zone).toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek)).atStartOfDay().toInstant(zone.getRules().getOffset(LocalDateTime.now())).getEpochSecond() * 1000;
    }

    /**
     * Gets the current time
     * @param zone The timezone to get the time from. This is usually the user's timezone
     * @return The Unix Epoch time in milliseconds for the specified timezone
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getCurrentTime(ZoneId zone)
    {
        return ZonedDateTime.now(zone).toInstant().getEpochSecond() * 1000;
    }

    /**
     * Gets the Calendar URI base
     * @param eventUri If true, the events URI will be used instead of the calendar URI
     * @return The base URI needed for accessing calendar events
     */
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
