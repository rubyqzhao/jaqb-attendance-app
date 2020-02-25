package com.example.jaqb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailsActivity extends AppCompatActivity {

    private String courseCode;
    private String courseName;
    private String courseDays;
    private String courseInstructor;
    private TextView code;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        System.out.println(courseCode);
        code = (TextView) findViewById(R.id.code);
        code.setText(courseCode);
    }
}