package com.example.jaqb;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.courses.CourseAdapter;

/**
 * @author amanjotsingh
 *
 * Activity class to display the courses for the logged-in user
 * */

public class MyCoursesActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView listView;
    private LoggedInUser currentUser;
    private Button searchCourses;
    private FireBaseDBServices fireBaseDBServices;
    private CourseAdapter courseAdapter;

    /**
     * @param savedInstanceState saved application context passed into activity when it is created
     */
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
        listView.setOnItemClickListener(this);
    }

    /**
     * @param v Event listener for the button click for search courses
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CourseRegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * @param parent parent view of the item clicked
     * @param view current view of the application
     * @param position position of the field selected in the current view
     * @param id id of the view which is selected by the user
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Course course = courseAdapter.getItem(position);

        Intent intent = new Intent(this, RegisteredCourseDetailsActivity.class)
                                    .setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra("code", course.getCode());
        intent.putExtra("name", course.getCourseName());
        intent.putExtra("instructor", course.getInstructorName());
        intent.putExtra("days", course.getDays());
        intent.putExtra("time", course.getTime());
        startActivity(intent);
        //based on item add info to intent
        //startActivity(intent);
    }
}
