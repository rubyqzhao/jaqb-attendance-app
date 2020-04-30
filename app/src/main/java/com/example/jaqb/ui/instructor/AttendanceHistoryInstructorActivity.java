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
import com.example.jaqb.ui.LogoutActivity;
import com.example.jaqb.ui.courses.CourseAdapter;
import com.example.jaqb.ui.student.AttendanceHistoryStudentActivity;


/**
 * @author amanjotsingh
 *
 * Activity class to handle the attendance history page for the users
 */

public class AttendanceHistoryInstructorActivity extends LogoutActivity
        implements AdapterView.OnItemClickListener {

    private FireBaseDBServices fireBaseDBServices;
    private LoggedInUser currentUser;
    private ListView listView;
    private CourseAdapter courseAdapter;

    /**
     * Triggers when the user first opens the activity. Initializes values for the
     * view to be displayed and the firebase.
     * @param savedInstanceState    the previous state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_attendance_history_instructor);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        listView = (ListView) findViewById(R.id.instructor_courses);
        findViewById(R.id.ins_progress_bar).setVisibility(View.GONE);
        courseAdapter = new CourseAdapter(this, currentUser.getRegisteredCourses());
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * Triggers when the user clicks on an item on the attendance history list.
     * Determines which detail page to direct the user to.
     * @param parent    the parent adapter view containing the item
     * @param view      the item view containing the attendance history
     * @param position  position of the item to be retrieved
     * @param id        id number of the item to be retrieved
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Course course = courseAdapter.getItem(position);
        Intent intent = new Intent();
        if("INSTRUCTOR".equals(currentUser.getLevel().toString())){
            intent.setClass(this, AttendanceHistoryOptionsActivity.class);
        }
        else if("STUDENT".equals(currentUser.getLevel().toString())){
            intent.setClass(this, AttendanceHistoryStudentActivity.class);
        }
        intent.putExtra("code", (String) course.getCode());
        startActivity(intent);
    }
}
