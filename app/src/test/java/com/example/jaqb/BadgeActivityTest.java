package com.example.jaqb;

import com.example.jaqb.data.model.Badge;
import com.example.jaqb.ui.student.CheckInActivity;
import com.example.jaqb.ui.student.BadgeActivity;
import com.example.jaqb.ui.student.BadgeAdapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test cases relating to the badges display activity for student rewards.
 *
 * @author Ruby Zhao
 * @version 1.0
 * @since 04-16-2020
 */

public class BadgeActivityTest {
    /**
     * Tests Badge model functions
     * @author Ruby Zhao
     */
    @Test
    public void checkBadgeModel() {
        Badge badge = new Badge(12345, "badge1", 1000);
        assertEquals(badge.getID(), 12345);
        assertEquals(badge.getName(), "badge1");
        assertEquals(badge.getImage(), 1000);
        badge.setID(100);
        badge.setName("badge2");
        badge.setImage(2000);
        assertEquals(badge.getID(), 100);
        assertEquals(badge.getName(), "badge2");
        assertEquals(badge.getImage(), 2000);
    }

    /**
     * Tests Badge Adapter functions
     * @author Ruby Zhao
     */
    @Test
    public void checkBadgeAdapter() {
        List<Badge> list = new ArrayList<Badge>();
        list.add(new Badge(1, "badge1", 1000));
        list.add(new Badge(2, "badge2", 1004));
        list.add(new Badge(3, "badge3", 1005));
        list.add(new Badge(4, "badge4", 1010));
        list.add(new Badge(5, "badge5", 1340));
        list.add(new Badge(6, "badge6", 1520));
        list.add(new Badge(7, "badge7", 1611));
        list.add(new Badge(8, "badge8", 2499));
        list.add(new Badge(9, "badge9", 3500));
        list.add(new Badge(10, "badge10", 10001));

        BadgeAdapter adapter = new BadgeAdapter(list);
        assertEquals(adapter.getCount(), 10);
    }

    /**
     * Verifies first attendance badge calculation
     * @author Ruby Zhao
     */
    @Test
    public void checkBadgeCalculationsFirstAttendance() {
        CheckInActivity checkin = new CheckInActivity();
        Map<String, Object> attendance = new HashMap<>();
        List<String> attendDates = new ArrayList<>();
        Map<String, Object> stats;

        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 0);
        assertEquals(stats.get("currentStreak"), 0);
        assertEquals(stats.get("totalClasses"), 0);

        attendDates.add("2020-01-15");
        attendance.put("2020-01-15", new Boolean(true));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 1);
        assertEquals(stats.get("currentStreak"), 1);
        assertEquals(stats.get("totalClasses"), 1);
    }

    /**
     * Verifies 5-streak badge statistics
     * @author Ruby Zhao
     */
    @Test
    public void checkBadgeCalculationsFiveStreak() {
        CheckInActivity checkin = new CheckInActivity();
        Map<String, Object> attendance = new HashMap<>();
        List<String> attendDates = new ArrayList<>();
        Map<String, Object> stats;

        //confirm 5-day streak
        attendDates.add("2020-01-15");
        attendDates.add("2020-01-17");
        attendDates.add("2020-01-20");
        attendDates.add("2020-01-23");
        attendDates.add("2020-01-25");
        attendance.put("2020-01-15", new Boolean(true));
        attendance.put("2020-01-17", new Boolean(true));
        attendance.put("2020-01-20", new Boolean(true));
        attendance.put("2020-01-23", new Boolean(true));
        attendance.put("2020-01-25", new Boolean(true));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 5);
        assertEquals(stats.get("currentStreak"), 5);
        assertEquals(stats.get("totalClasses"), 5);

        attendance.clear();
        attendance.put("2020-01-15", new Boolean(false));
        attendance.put("2020-01-17", new Boolean(false));
        attendance.put("2020-01-20", new Boolean(false));
        attendance.put("2020-01-23", new Boolean(false));
        attendance.put("2020-01-25", new Boolean(false));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 0);
        assertEquals(stats.get("currentStreak"), 0);
        assertEquals(stats.get("totalClasses"), 5);

        //confirm broken streak
        attendance.clear();
        attendance.put("2020-01-15", new Boolean(false));
        attendance.put("2020-01-17", new Boolean(true));
        attendance.put("2020-01-20", new Boolean(true));
        attendance.put("2020-01-23", new Boolean(true));
        attendance.put("2020-01-25", new Boolean(true));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 4);
        assertEquals(stats.get("currentStreak"), 4);
        assertEquals(stats.get("totalClasses"), 5);

        attendance.clear();
        attendance.put("2020-01-15", new Boolean(false));
        attendance.put("2020-01-17", new Boolean(true));
        attendance.put("2020-01-20", new Boolean(true));
        attendance.put("2020-01-23", new Boolean(true));
        attendance.put("2020-01-25", new Boolean(false));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 3);
        assertEquals(stats.get("currentStreak"), 3);
        assertEquals(stats.get("totalClasses"), 5);
    }

    /**
     * Verifies perfect attendance stat operations
     * @author Ruby Zhao
     */
    @Test
    public void checkBadgeCalculationsPerfectAttendance() {
        CheckInActivity checkin = new CheckInActivity();
        Map<String, Object> attendance = new HashMap<>();
        List<String> attendDates = new ArrayList<>();
        Map<String, Object> stats;

        attendDates.add("2020-01-15");
        attendDates.add("2020-01-17");
        attendDates.add("2020-01-20");
        attendDates.add("2020-01-23");
        attendDates.add("2020-01-25");
        attendDates.add("2020-01-28");
        attendDates.add("2020-02-01");
        attendDates.add("2020-02-03");
        attendDates.add("2020-02-05");
        attendance.put("2020-01-15", new Boolean(false));
        attendance.put("2020-01-17", new Boolean(false));
        attendance.put("2020-01-20", new Boolean(true));
        attendance.put("2020-01-23", new Boolean(true));
        attendance.put("2020-01-25", new Boolean(true));
        attendance.put("2020-01-28", new Boolean(true));
        attendance.put("2020-02-01", new Boolean(false));
        attendance.put("2020-02-03", new Boolean(true));
        attendance.put("2020-02-05", new Boolean(true));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 6);
        assertEquals(stats.get("currentStreak"), 4);
        assertEquals(stats.get("totalClasses"), 9);

        attendance.clear();
        attendance.put("2020-01-15", new Boolean(true));
        attendance.put("2020-01-17", new Boolean(true));
        attendance.put("2020-01-20", new Boolean(true));
        attendance.put("2020-01-23", new Boolean(true));
        attendance.put("2020-01-25", new Boolean(true));
        attendance.put("2020-01-28", new Boolean(true));
        attendance.put("2020-02-01", new Boolean(true));
        attendance.put("2020-02-03", new Boolean(true));
        attendance.put("2020-02-05", new Boolean(true));
        stats = checkin.badgeStats(attendDates, attendance);
        assertEquals(stats.get("numAttended"), 9);
        assertEquals(stats.get("currentStreak"), 9);
        assertEquals(stats.get("totalClasses"), 9);
    }

}
