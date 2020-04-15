package com.example.jaqb;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

import com.example.jaqb.services.InstructorServicesHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstructorServicesHelperTest {

    private InstructorServicesHelper instructorServicesHelper = new InstructorServicesHelper();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.jaqb", appContext.getPackageName());
    }

    @Test
    public void TestGenerateRandomCode_ShouldGenerateRandomInteger(){
        int randomNumber1 = instructorServicesHelper.generateRandomCode();
        int randomNumber2 = instructorServicesHelper.generateRandomCode();

        assertFalse("Two consequently generated random numbers are different", randomNumber1 == randomNumber2);
    }

    @Test
    public void TestGenerateRandomCode_ShouldGenerateFourDigitInt(){
        boolean allNumbersAreFourDigited = true;
        for(int i = 0; i < 100; i++){
            int randomNumber = instructorServicesHelper.generateRandomCode();
            int count = 0;
            while (randomNumber != 0) {
                randomNumber = randomNumber / 10;
                ++count;
            }
            if(count != 4){
                allNumbersAreFourDigited = false;
            }
        }

        assertTrue("Random numbers generated over 100 times are all of 4 digits", allNumbersAreFourDigited);
    }

    @Test
    public void TestIsPreviousCodeValid_FutureTime_ShouldPass(){
        Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime= formatter.format(date);
        boolean isCodeValid = instructorServicesHelper.isPreviousCodeValid(currentTime, TimeUnit.HOURS);

        assertFalse("Current date is validated with Future time", isCodeValid);
    }

    @Test
    public void TestIsPreviousCodeValid_PastTime_ShouldPass(){
        Date date = new Date(System.currentTimeMillis() - 3600 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime= formatter.format(date);
        boolean isCodeValid = instructorServicesHelper.isPreviousCodeValid(currentTime, TimeUnit.HOURS);

        assertFalse("Current date is validated with past date", isCodeValid);
    }

    @Test
    public void TestIsPreviousCodeValid_InCompleteDate_ShouldThrowException(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(date);
        boolean isCodeValid = instructorServicesHelper.isPreviousCodeValid(currentTime, TimeUnit.HOURS);
        assertFalse(isCodeValid);
    }

    @Test
    public void TestIsPreviousCodeValid_InvalidDateFormat_ShouldFail(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy ss:HH:mm");
        String currentTime = formatter.format(date);
        boolean isCodeValid = instructorServicesHelper.isPreviousCodeValid("1234 " + currentTime, TimeUnit.HOURS);
        assertFalse("Code validation using invalid date format", isCodeValid);
    }
}
