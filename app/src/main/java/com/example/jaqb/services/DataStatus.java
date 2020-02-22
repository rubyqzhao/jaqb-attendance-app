package com.example.jaqb.services;

import com.example.jaqb.data.model.Course;

import java.util.List;

public interface DataStatus {
    void dataIsLoaded(List<Course> courses, List<String> keys);
    void dataIsInserted();
    void dataIsUpdated();
    void dataIsDeleted();
}