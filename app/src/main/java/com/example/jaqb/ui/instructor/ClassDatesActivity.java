package com.example.jaqb.ui.instructor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.R;
import com.example.jaqb.services.FireBaseDBServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassDatesActivity extends AppCompatActivity {

    private String courseCode;
    private List<String> courseDates;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_dates);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        databaseReference = FirebaseDatabase.getInstance().getReference("InstructorAttendance")
                .child(courseCode);
        courseDates = new ArrayList<>();
        listView = (ListView) findViewById(R.id.dates_course_list);
        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item, R.id.class_item, courseDates);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = (String) keyNode.getKey();
                    courseDates.add(date);
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
