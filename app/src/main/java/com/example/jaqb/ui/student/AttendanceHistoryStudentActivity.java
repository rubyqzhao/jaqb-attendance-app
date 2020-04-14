package com.example.jaqb.ui.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.R;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold the activity that shows the attendance history of an user
 */
public class AttendanceHistoryStudentActivity extends AppCompatActivity {

    private String courseCode;
    private DatabaseReference databaseReference;
    private LoggedInUser currentUser;
    private FireBaseDBServices fireBaseDBServices;
    private List<String> courseAttendance;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState the previous state of app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_history_calendar);
        courseAttendance = new ArrayList<>();
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(currentUser.getuID())
                .child("attendanceHistory")
                .child(courseCode);

//        listView = (ListView) findViewById(R.id.dates_course_list);
//        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item,
//                R.id.class_item_name, courseAttendance);
        databaseReference.addValueEventListener(new ValueEventListener() {
            /**
             * Triggers when there has to a change in Database
             * @param dataSnapshot the current state of database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = keyNode.getKey();
                    boolean presence = (boolean) keyNode.getValue();
                    courseAttendance.add(date + " : " + (presence?"Present" : "Absent"));
                }
//                listView.setAdapter(arrayAdapter);
            }

            /**
             * Triggers if data fails to get updated
             * @param databaseError the error due to which change could not happen
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
