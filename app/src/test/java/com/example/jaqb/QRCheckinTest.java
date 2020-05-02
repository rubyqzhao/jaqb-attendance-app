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
        Date date = new Date(System.currentTimeMillis() + 12*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String earlyTime = formatter.format(date);
        String earlyTimeStatus = qrCheckin.getCheckTimeString(earlyTime);
        assertTrue(earlyTimeStatus.equals("true"));
    }
    

    @Test
    public void TestCheckTimeString_ShouldReturnEarly() {
        Date date = new Date(System.currentTimeMillis() + 20*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String earlyTime = formatter.format(date);
        String earlyTimeStatus = qrCheckin.getCheckTimeString(earlyTime);
        assertTrue(earlyTimeStatus.equals("early"));
    }

    @Test
    public void TestCheckTimeString_ShouldReturnLate() {
        Date date = new Date(System.currentTimeMillis() - 12*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String lateTime = formatter.format(date);
        String lateTimeStatus = qrCheckin.getCheckTimeString(lateTime);
        assertTrue(lateTimeStatus.equals("late"));
    }

    @Test
    public void TestCheckTimeString_ShouldReturnTooLate() {
        Date date = new Date(System.currentTimeMillis() - 30*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String tooLateTime = formatter.format(date);
        String tooLateTimeStatus = qrCheckin.getCheckTimeString(tooLateTime);
        assertTrue(tooLateTimeStatus.equals("tooLate"));
    }
}