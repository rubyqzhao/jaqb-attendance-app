package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.CourseDetailsActivity;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.RegisteredUser;
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

public class AttendanceHistoryByNamesActivity extends AppCompatActivity {

    private List<RegisteredUser> studentsAttendance;
    private List<String> studentIds;
    private List<String> studentNames;
    private Map<String, RegisteredUser> studentData;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;
    private String courseCode;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_class_dates);
        courseCode = (String) getIntent().getCharSequenceExtra("code");

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        studentsAttendance = new ArrayList<RegisteredUser>();
        studentIds = new ArrayList<String>();
        studentNames = new ArrayList<String>();
        studentData = new HashMap<String, RegisteredUser>();
        listView = (ListView) findViewById(R.id.dates_course_list);
        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item, R.id.class_item_name, studentNames);


        Query query = FirebaseDatabase.getInstance().getReference("Course").orderByChild("code")
                .equalTo(courseCode);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String key = keyNode.getKey();
                    for(DataSnapshot node : keyNode.getChildren()){
                        if(node.getKey().equalsIgnoreCase("students")){
                            for(DataSnapshot shots : node.getChildren()){
                                studentIds.add((String) shots.getKey());
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = arrayAdapter.getItem(position);
                String item_id = item.split(":")[1].trim();
                Intent intent = new Intent(getApplicationContext(), AttendanceHistoryIndividualStudentActivity.class);
                intent.putExtra("studentId", item_id.substring(0, item_id.length() - 1));
                intent.putExtra("code", courseCode);
                startActivity(intent);
            }
        });
    }

    private void getDisplayDate(){
        for(String id : studentIds){
            if(studentData.containsKey(id)){
                RegisteredUser user = studentData.get(id);
                studentNames.add(user.getfName() + " " + user.getlName() + " (Id : " + id + ")");
            }
        }
//        for(int i=0; i< studentsAttendance.size(); i++){
//            RegisteredUser user = studentsAttendance.get(i);
//            studentNames.add(user.getfName() + " " + user.getlName());
//        }
    }
}
