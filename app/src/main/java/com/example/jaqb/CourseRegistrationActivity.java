package com.example.jaqb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.LogoutActivity;
import com.example.jaqb.ui.courses.CourseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amanjotsingh
 *
 * Activity class to list out all courses and provide the functionality to search courses
 * */

public class CourseRegistrationActivity extends LogoutActivity implements
        SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener {

    private FireBaseDBServices fireBaseDBServices;
    private ListView listView;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    private SearchView searchView;

    /**
     * @param savedInstanceState saved application context passed into the activity at the time
     *                           it is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_course_registration);
        findViewById(R.id.my_progressBar).setVisibility(View.GONE);
        courseList = new ArrayList<>();
        fireBaseDBServices = FireBaseDBServices.getInstance();
        courseList.addAll(fireBaseDBServices.getAllCourses());
        listView = (ListView) findViewById(R.id.my_course_list);
        searchView = (SearchView) findViewById(R.id.seach);
        courseAdapter = new CourseAdapter(this, courseList);
        listView.setAdapter(courseAdapter);
        searchView.setOnQueryTextListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * @param newText text added in the search bar for the courses page
     * @return true and sets the text in the filtered list
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        courseAdapter.getFilter().filter(newText);
        return true;
    }

    /**
     * @param parent parent view of the item clicked
     * @param view current view of the application
     * @param position position of the field selected in the current view
     * @param id id of the view which is selected by the user
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Course course = courseAdapter.getItem(position);
        intent.setClass(this, CourseDetailsActivity.class);
        intent.putExtra("code", (String) course.getCode());
        intent.putExtra("name", (String) course.getCourseName());
        intent.putExtra("instructor", (String) course.getInstructorName());
        intent.putExtra("days", (String) course.getDays());
        intent.putExtra("time", (String) course.getTime());
        if(fireBaseDBServices.courseAlreadyRegistered(course.getCode())){
            intent.putExtra("registered", "true");
        }
        else{
            intent.putExtra("registered", "false");
        }
        startActivity(intent);
    }
}
