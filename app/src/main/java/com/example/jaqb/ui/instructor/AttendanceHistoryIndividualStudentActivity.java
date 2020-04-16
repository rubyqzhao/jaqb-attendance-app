package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryIndividualStudentActivity extends AppCompatActivity {

    private String courseCode;
    private String studentId;
    private DatabaseReference databaseReference;
    private List<String> courseAttendance;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_dates);
        courseAttendance = new ArrayList<>();
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        studentId = (String) getIntent().getCharSequenceExtra("studentId");
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(studentId)
                .child("attendanceHistory")
                .child(courseCode);

        listView = (ListView) findViewById(R.id.dates_course_list);
        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item,
                R.id.class_item_name, courseAttendance);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = keyNode.getKey();
                    boolean presence = (boolean) keyNode.getValue();
                    courseAttendance.add(date + " : " + (presence?"Present" : "Absent"));
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
