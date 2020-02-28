package com.example.jaqb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;

/**
 * @author amanjotsingh
 *
 * This activity class displays the details of the course and helps user register
 * for that course.
 * */

public class CourseDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String courseCode;
    private String courseName;
    private String courseDays;
    private String courseInstructor;
    private String time;
    private TextView courseCodeTextView;
    private TextView courseNameTextView;
    private TextView courseDaysTextView;
    private TextView courseInstructorTextView;
    private TextView courseLocation;
    private TextView courseTime;
    private Button registerButton;
    private DialogInterface.OnClickListener dialogClickListener;
    private FireBaseDBServices fireBaseDBServices;
    private LoggedInUser currentUser;
    private Course registerCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        courseName = (String) getIntent().getCharSequenceExtra("name");
        courseDays = (String) getIntent().getCharSequenceExtra("days");
        courseInstructor = (String) getIntent().getCharSequenceExtra("instructor");
        time = (String) getIntent().getCharSequenceExtra("time");
        String registered = (String) getIntent().getCharSequenceExtra("registered");

        courseCodeTextView = (TextView) findViewById(R.id.code);
        courseNameTextView = (TextView) findViewById(R.id.name);
        courseDaysTextView = (TextView) findViewById(R.id.days);
        courseInstructorTextView = (TextView) findViewById(R.id.instructor);
        courseLocation = (TextView) findViewById(R.id.location);
        courseTime = (TextView) findViewById(R.id.time);

        courseCodeTextView.setText(courseCode);
        courseNameTextView.setText(courseName);
        courseDaysTextView.setText(courseDays);
        courseInstructorTextView.setText(courseInstructor);
        courseLocation.setText("Poly AGBC 150");
        courseTime.setText(time);

        registerCourse = new Course();
        registerCourse.setDays(courseDays);
        registerCourse.setCode(courseCode);
        registerCourse.setInstructorName(courseInstructor);
        registerCourse.setCourseName(courseName);
        registerCourse.setTime(time);

        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();


        registerButton = (Button) findViewById(R.id.register);
        if("true".equalsIgnoreCase(registered)){
            registerButton.setEnabled(false);
            Toast.makeText(this, "Already Registered", Toast.LENGTH_LONG).show();
        }
        else if("false".equalsIgnoreCase(registered)){
            registerButton.setOnClickListener(this);
        }

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // update database with this course for that user
                        int res = fireBaseDBServices.registerCourse(registerCourse, currentUser);
                        Intent intent = new Intent();
                        if(res == 1){
                            System.out.println("Registered in new course");
                            currentUser.updateCourse(registerCourse);
                            intent.setClass(getApplicationContext(), MyCoursesActivity.class);
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();

                        }
                        else if(res == 0){
                            System.out.println("Error in registering course");
                            intent.setClass(getApplicationContext(), CourseRegistrationActivity.class);
                            break;
                        };
                        startActivity(intent);
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Register for " + courseCode + "?").setPositiveButton("Register", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }
}