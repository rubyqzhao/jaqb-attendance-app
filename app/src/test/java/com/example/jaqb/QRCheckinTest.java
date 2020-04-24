package com.example.jaqb;

import android.text.format.DateUtils;

import com.example.jaqb.ui.student.QRCheckin;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class QRCheckinTest {

    //create class object here
    private QRCheckin qrCheckin = new QRCheckin();

    @Test
    public void TestCheckTimeString_ShouldReturnTrue() {

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String onTimeStatus = qrCheckin.getCheckTimeString(currentTime);
        assertEquals(true, onTimeStatus.equals("true"));
    }

    @Test
    public void  TestCheckTimeString_ShouldAlsoReturnTrueInLittleEarly() {
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE, 10);
        Date newDate = now.getTime();
        String earlyTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(newDate);
        String earlyTimeStatus = qrCheckin.getCheckTimeString(earlyTime);
        assertTrue(earlyTimeStatus.equals("true"));
    }
    

    @Test
    public void TestCheckTimeString_ShouldReturnEarly() {
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE, 20);
        Date newDate = now.getTime();
        String earlyTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(newDate);
        String earlyTimeStatus = qrCheckin.getCheckTimeString(earlyTime);
        assertTrue(earlyTimeStatus.equals("early"));
    }

    @Test
    public void TestCheckTimeString_ShouldReturnLate() {
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE, -12);
        Date newDate = now.getTime();
        String lateTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(newDate);
        String lateTimeStatus = qrCheckin.getCheckTimeString(lateTime);
        assertTrue(lateTimeStatus.equals("late"));
    }

    @Test
    public void TestCheckTimeString_ShouldReturnTooLate() {
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE, -20);
        Date newDate = now.getTime();
        String tooLateTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(newDate);
        String tooLateTimeStatus = qrCheckin.getCheckTimeString(tooLateTime);
        assertTrue(tooLateTimeStatus.equals("tooLate"));
    }
}