package com.example.jaqb;

import com.example.jaqb.services.InstructorServicesHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class InstructorServicesHelperUnitTest {

    private InstructorServicesHelper instructorServicesHelper = new InstructorServicesHelper();

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
    public void TestIsPreviousCodeValid_ValidData_ShouldReturnTrue(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(date);
        boolean isCodeValid = false;
        try {
            isCodeValid = instructorServicesHelper.isPreviousCodeValid("1234 " + currentTime, TimeUnit.HOURS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(isCodeValid);
    }

    @Test
    public void TestIsPreviousCodeValid_ValidData_ShouldReturnFalse(){
        Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(date);
        boolean isCodeValid = false;
        try {
            isCodeValid = instructorServicesHelper.isPreviousCodeValid("1234 " + currentTime, TimeUnit.HOURS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertFalse(isCodeValid);
    }

    @Test
    public void TestIsPreviousCodeValid_InvalidFormat_ShouldThrowArrayIndexOutOfBoundException(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String currentTime= formatter.format(date);
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                instructorServicesHelper.isPreviousCodeValid(currentTime, TimeUnit.HOURS);
            }
        });

        String expectedMessage = "ArrayIndexOutOfBoundsException";
        String actualMessage = exception.toString();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void TestIsPreviousCodeValid_InvalidInputData_ShouldThrowParseException(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String currentTime= formatter.format(date);
        Exception exception = assertThrows(ParseException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                instructorServicesHelper.isPreviousCodeValid("1123 1123 " + currentTime, TimeUnit.HOURS);
            }
        });

        String expectedMessage = "ParseException";
        String actualMessage = exception.toString();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
