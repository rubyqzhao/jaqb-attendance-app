package com.example.jaqb.services;

import java.util.Random;

public class InstructorServicesHelper {

    public int generateRandomCode(){
        int res = 0;
        Random random = new Random();
        int low = 1000;
        int high = 10000;
        res = random.nextInt(high-low) + low;
        return res;
    }
}
