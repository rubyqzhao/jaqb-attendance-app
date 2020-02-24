package com.example.jaqb;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.courses.CourseAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView listView;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registration);
        courseList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Course");
        listView = (ListView) findViewById(R.id.course_list);
        courseAdapter = new CourseAdapter(this, courseList);
        listView.setAdapter(courseAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                List<String> keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Course course = keyNode.getValue(Course.class);
                    courseList.add(course);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
