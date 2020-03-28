package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.courses.CourseAdapter;
import com.example.jaqb.ui.student.AttendanceHistoryStudentActivity;

/**
 * @author amanjotsingh
 *
 * Activity class to handle the attendance history page for the users
 */

public class AttendanceHistoryInstructorActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private FireBaseDBServices fireBaseDBServices;
    private LoggedInUser currentUser;
    private ListView listView;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history_instructor);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        listView = (ListView) findViewById(R.id.instructor_courses);
        findViewById(R.id.ins_progress_bar).setVisibility(View.GONE);
        courseAdapter = new CourseAdapter(this, currentUser.getRegisteredCourses());
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Course course = courseAdapter.getItem(position);
        Intent intent = new Intent();
        if("INSTRUCTOR".equals(currentUser.getLevel().toString())){
            intent.setClass(this, ClassDatesActivity.class);
        }
        else if("STUDENT".equals(currentUser.getLevel().toString())){
            intent.setClass(this, AttendanceHistoryStudentActivity.class);
        }
        intent.putExtra("code", (String) course.getCode());
        startActivity(intent);
    }
}
