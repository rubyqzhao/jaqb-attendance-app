package com.example.jaqb.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.ui.menu.MenuOptionsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.jaqb.CourseRegistrationActivity;
import com.example.jaqb.IncompleteActivity;
import com.example.jaqb.MainActivity;
import com.example.jaqb.R;
import com.example.jaqb.ui.instructor.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends MenuOptionsActivity {

    private TextView upcomingClass;
    private DatabaseReference databaseReference;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        Toolbar myToolbar = findViewById(R.id.checkin_toolbar);
        setSupportActionBar(myToolbar);
        upcomingClass = findViewById(R.id.upcoming_class);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Course");
        courseList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener courseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Course course = keyNode.getValue(Course.class);
                    courseList.add(course);
                }
                upcomingClass.setText(determineClassToDisplay());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        databaseReference.addValueEventListener(courseListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        upcomingClass.setText(determineClassToDisplay());
    }

    public void checkinButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void setAlarmButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void seeRewardsButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void seeAttendanceButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    protected String determineClassToDisplay() {
        String message;
        //todo: change decision logic to get closest upcoming class
        if(!courseList.isEmpty()) {
            Course course = courseList.get(1);
            String code = course.getCode();
            String days = course.getDays();

            message = code + "\n" + days;
        }
        else {
            message = "Course list is empty";
        }

        return message;
    }
}
