package com.example.jaqb;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.data.model.RegisteredUser;
import com.example.jaqb.data.model.Semester;
import com.example.jaqb.data.model.SemesterDate;
import com.example.jaqb.data.model.UserLevel;
import com.example.jaqb.services.CalendarServices;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing for the CalendarServices class
 *
 * @author Joshua Drumm
 * @version 1.0
 * @since   2020-4-17
 */
public class CalendarServicesTest {
    @Test
    public void checkDaysOfWeek()
    {
        assertEquals(DayOfWeek.MONDAY, CalendarServices.getDayOfWeek("MO"));
        assertEquals(DayOfWeek.TUESDAY, CalendarServices.getDayOfWeek("TU"));
        assertEquals(DayOfWeek.WEDNESDAY, CalendarServices.getDayOfWeek("WE"));
        assertEquals(DayOfWeek.THURSDAY, CalendarServices.getDayOfWeek("TH"));
        assertEquals(DayOfWeek.FRIDAY, CalendarServices.getDayOfWeek("FR"));
        assertEquals(DayOfWeek.SATURDAY, CalendarServices.getDayOfWeek("SA"));
        assertEquals(DayOfWeek.SUNDAY, CalendarServices.getDayOfWeek("SU"));
    }

    @Test
    public void checkTimeOfNextDayCode()
    {
        ZoneId zone = ZoneId.of("America/Phoenix");
        assertEquals(DayOfWeek.MONDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("MO", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("TU", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.WEDNESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("WE", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("TH", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("FR", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("SA", zone)), zone).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getTimeOfNextDayCode("SU", zone)), zone).getDayOfWeek());
    }

    @Test
    public void checkCurrentTime()
    {
        ZoneId zone = ZoneId.of("America/Phoenix");
        assertEquals(ZonedDateTime.now(zone).getDayOfWeek(), ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getCurrentTime(zone)), zone).getDayOfWeek());
    }

    @Test
    public void checkTimeOfDayCodeSemester()
    {
        for(int i = 0; i < 100; i++) {
            ZoneId zone = ZoneId.of("America/Phoenix");
            LoggedInUser user = new LoggedInUser(null, new RegisteredUser("No", "Name", UserLevel.STUDENT, new ArrayList<String>()));
            SemesterDate startDate = new SemesterDate((int) (Math.random() * 12 + 1) + "-" + (int) (Math.random() * 28 + 1) + "-" + (int) (Math.random() * 200 + 1901));
            user.setSemester(new Semester("America/Phoenix", startDate, new SemesterDate("1-1-2020"), null));
            assertEquals(DayOfWeek.MONDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("MO", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.TUESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("TU", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.WEDNESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("WE", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.THURSDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("TH", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.FRIDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("FR", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.SATURDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("SA", user)), zone).getDayOfWeek());
            assertEquals(DayOfWeek.SUNDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                    CalendarServices.getNextDaySemester("SU", user)), zone).getDayOfWeek());
        }
    }

    @Test
    public void testNextCourse()
    {
        ZoneId zone = ZoneId.of("America/Phoenix");
        LoggedInUser user = new LoggedInUser(null, new RegisteredUser("No", "Name", UserLevel.STUDENT, new ArrayList<String>()));
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("CSE 1", "Test 1", "TH", "18:00"));
        courses.add(new Course("CSE 2", "Test 2", "TU", "9:00"));
        courses.add(new Course("CSE 3", "Test 3", "MO,WE", "9:00"));
        courses.add(new Course("CSE 4", "Test 4", "MO,WE", "12:00"));
        user.setRegisteredCourses(courses);
        SemesterDate startDate = new SemesterDate((int) (Math.random() * 12 + 1) + "-" + (int) (Math.random() * 28 + 1) + "-" + (int) (Math.random() * 200 + 1901));
        user.setSemester(new Semester("America/Phoenix", startDate, new SemesterDate("1-1-2020"), null));
        ZonedDateTime now = ZonedDateTime.now(zone);
        switch(now.getDayOfWeek()) {
            case MONDAY :
                if (now.getHour() < 9)
                    assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else if (now.getHour() < 12)
                    assertEquals(courses.get(3).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else
                    assertEquals(courses.get(1).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                assertEquals(courses.get(1), CalendarServices.getNextCourse(courses.subList(0, 1), user));
                break;
            case TUESDAY :
                if (now.getHour() < 9)
                    assertEquals(courses.get(1).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else
                    assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses.subList(2, 3), user).getCourseName());
                break;
            case WEDNESDAY :
                if (now.getHour() < 9)
                    assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else if (now.getHour() < 12)
                    assertEquals(courses.get(3).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else
                    assertEquals(courses.get(0).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                assertEquals(courses.get(0), CalendarServices.getNextCourse(courses.subList(0, 1), user));
                break;
            case THURSDAY :
                if (now.getHour() < 18)
                    assertEquals(courses.get(0).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                else
                {
                    assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses, user).getCourseName());
                    assertEquals(courses.get(1).getCourseName(), CalendarServices.getNextCourse(courses.subList(0, 1), user).getCourseName());
                }
                assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses.subList(2, 3), user).getCourseName());
                break;
            default:
                assertEquals(courses.get(2).getCourseName(), CalendarServices.getNextCourse(courses.subList(2, 3), user).getCourseName());
                assertEquals(courses.get(1).getCourseName(), CalendarServices.getNextCourse(courses.subList(1, 2), user).getCourseName());
        }
    }

    @Test
    public void getNextCourseTime()
    {
        ZoneId zone = ZoneId.of("America/Phoenix");
        assertEquals(DayOfWeek.MONDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "MO", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "TU", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.WEDNESDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "WE", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "TH", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "FR", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "SA", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, ZonedDateTime.ofInstant(Instant.ofEpochMilli(
                CalendarServices.getNextTime(new Course("", "", "SU", "18:00"),
                        "America/Phoenix")), zone).getDayOfWeek());
    }
}