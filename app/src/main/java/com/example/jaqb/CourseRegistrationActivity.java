package com.example.jaqb;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.services.DataStatus;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.courses.RecyclerViewConfig;

import java.lang.reflect.Array;
import java.util.List;

public class CourseRegistrationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText filter;
    private List<Course> courseList;
    private RecyclerViewConfig.CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registration);
        recyclerView = (RecyclerView) findViewById(R.id.course_list);
        filter = (EditText) findViewById(R.id.search);
        recyclerView.setAdapter(adapter);
        new FireBaseDBServices().getAllCourses(new DataStatus() {
            @Override
            public void dataIsLoaded(List<Course> courses, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerViewConfig().setConfig(recyclerView, CourseRegistrationActivity.this,
                        courses, keys);
            }

            @Override
            public void dataIsInserted() {

            }

            @Override
            public void dataIsUpdated() {

            }

            @Override
            public void dataIsDeleted() {

            }
        });
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
