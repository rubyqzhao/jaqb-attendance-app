package com.example.jaqb.services;

import com.example.jaqb.data.model.Course;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class InstructorServicesHelper {

    public int generateRandomCode() {
        int res = 0;
        Random random = new Random();
        int low = 1000;
        int high = 10000;
        res = random.nextInt(high - low) + low;
        return res;
    }

    public boolean isPreviousCodeValid(String previousTime, TimeUnit timeUnit){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime= formatter.format(date);
        String[] prevData = previousTime.split(" ");
        try {
            Date prevDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(prevData[1] + " " + prevData[2]);
            Date nowDate = formatter.parse(currentTime);
            long timeDiff = nowDate.getTime() - prevDate.getTime();
            long diff = timeUnit.convert(timeDiff, TimeUnit.HOURS);
            System.out.println("DIFFERENCE IN TIME UNITS : " + diff);
            long timeInHours = (diff)/(1000*60*60);
            if(timeInHours < 24){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
