package com.example.jaqb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
    private TextView courseCodeTextView;
    private TextView courseNameTextView;
    private TextView courseDaysTextView;
    private TextView courseInstructorTextView;
    private Button registerButton;
    private DialogInterface.OnClickListener dialogClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        courseName = (String) getIntent().getCharSequenceExtra("name");
        courseDays = (String) getIntent().getCharSequenceExtra("days");
        courseInstructor = (String) getIntent().getCharSequenceExtra("instructor");

        courseCodeTextView = (TextView) findViewById(R.id.code);
        courseNameTextView = (TextView) findViewById(R.id.name);
        courseDaysTextView = (TextView) findViewById(R.id.days);
        courseInstructorTextView = (TextView) findViewById(R.id.instructor);

        courseCodeTextView.setText(courseCode);
        courseNameTextView.setText(courseName);
        courseDaysTextView.setText(courseDays);
        courseInstructorTextView.setText(courseInstructor);

        registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(this);
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // update database with this course for that user
                        break;

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