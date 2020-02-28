package com.example.jaqb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.courses.CourseAdapter;

/**
 * @author amanjotsingh
 *
 * Activity class to display the courses for the logged-in user
 * */

public class MyCoursesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private LoggedInUser currentUser;
    private Button searchCourses;
    private FireBaseDBServices fireBaseDBServices;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        searchCourses = (Button) findViewById(R.id.more_courses);
        listView = (ListView) findViewById(R.id.my_course_list);
        findViewById(R.id.my_progressBar).setVisibility(View.GONE);
        courseAdapter = new CourseAdapter(this, currentUser.getRegisteredCourses());
        listView.setAdapter(courseAdapter);
        searchCourses.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CourseRegistrationActivity.class);
        startActivity(intent);
    }
}
