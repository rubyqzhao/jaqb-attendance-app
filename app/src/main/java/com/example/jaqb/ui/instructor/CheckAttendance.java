package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
import com.example.jaqb.data.model.RegisteredUser;
import com.example.jaqb.data.model.User;
import com.example.jaqb.services.FireBaseDBServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAttendance extends AppCompatActivity {

    private String courseCode;
    private String date;
    private List<RegisteredUser> studentsAttendance;
    private List<String> studentIds;
    private List<String> studentPresence;
    private List<String> studentNames;
    private Map<String, RegisteredUser> studentData;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_dates);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        date = (String) getIntent().getCharSequenceExtra("date");
        databaseReference = FirebaseDatabase.getInstance().getReference("InstructorAttendance")
                .child(courseCode).child(date);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("User");
        studentsAttendance = new ArrayList<RegisteredUser>();
        studentIds = new ArrayList<String>();
        studentPresence = new ArrayList<String>();
        studentNames = new ArrayList<String>();
        studentData = new HashMap<String, RegisteredUser>();
        listView = (ListView) findViewById(R.id.dates_course_list);
        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item, R.id.class_item_name, studentNames);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    String studentId = keyNode.getKey();
                    boolean attendance = (boolean) keyNode.getValue();
                    studentIds.add(studentId);
                    studentPresence.add((attendance) ? "Present" : "Absent");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String key = keyNode.getKey();
                    String fname = (String) keyNode.child("fname").getValue();
                    String lname = (String) keyNode.child("lname").getValue();
                    RegisteredUser user = new RegisteredUser(fname, lname);
                    studentData.put(key, user);
                }
                getDisplayDate();
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDisplayDate(){
        for(String id : studentIds){
            if(studentData.containsKey(id)){
                studentsAttendance.add(studentData.get(id));
            }
        }
        for(int i=0; i< studentsAttendance.size(); i++){
            RegisteredUser user = studentsAttendance.get(i);
            String presence = studentPresence.get(i);
            studentNames.add(user.getfName() + " " + user.getlName() + " : " + presence);
        }
    }
}
