package com.example.jaqb.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author amanjotsingh
 *
 * This class is a helper class for generating the QR code for the instructor
 */
public class InstructorServicesHelper {

    /**
     * @return 4 digit random code generated for the QR code
     */
    public int generateRandomCode() {
        int res = 0;
        Random random = new Random();
        int low = 1000;
        int high = 10000;
        res = random.nextInt(high - low) + low;
        return res;
    }

    /**
     * This method checks if the previous QR code is valid as the validity of the QR code
     * generated is 24 hours
     *
     * @param previousTime last time the instructor generated the QR code
     * @param timeUnit Units in which the difference in the times of QR code generation is expected
     * @return true if the previous code is valid, false if it is invalid
     */
    public boolean isPreviousCodeValid(String previousTime, TimeUnit timeUnit)
            throws ArrayIndexOutOfBoundsException, ParseException
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime= formatter.format(date);
        String[] prevData = previousTime.split(" ");
        Date prevDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(prevData[1] + " " + prevData[2]);
        Date nowDate = formatter.parse(currentTime);
        long timeDiff = nowDate.getTime() - prevDate.getTime();
        long diff = timeUnit.convert(timeDiff, TimeUnit.HOURS);
        System.out.println("DIFFERENCE IN TIME UNITS : " + diff);
        long timeInHours = (diff)/(1000*60*60);
        if(timeInHours < 24 && timeInHours >= 0){
            return true;
        }
        return false;
    }
}
