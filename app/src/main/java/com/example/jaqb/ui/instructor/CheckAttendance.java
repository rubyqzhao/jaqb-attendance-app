package com.example.jaqb.ui.instructor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * A view class to provide functions for the instructor attendance page
 * to show the lists of students who have attended their courses.
 *
 * @author Amanjot Singh
 * @version 1.0
 */
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

    /**
     * Initial method that triggers when the user accesses the attendance list
     * for the first time in the app. Creates the view and initializes values
     * using data pulled from firebase.
     * @param savedInstanceState    the previous state of the app
     */
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
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.class_list_item, R.id.class_item_name, studentNames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView)view.findViewById(R.id.class_item_name);
                String text = textView.getText().toString();//setTextColor(position % 2 == 0 ? Color.WHITE : Color.RED); // here can be your logic
                String color = text.split(":")[1].trim();
                if("late".equalsIgnoreCase(color)){
                    textView.setTextColor(Color.YELLOW);
                }
                else if("present".equalsIgnoreCase(color)) {
                    textView.setTextColor(Color.GREEN);
                }
                else if("absent".equalsIgnoreCase(color)){
                    textView.setTextColor(Color.RED);
                }
                return view;
            };
        };
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    String studentId = keyNode.getKey();
                    String attendance = (String) keyNode.getValue();
                    studentIds.add(studentId);
                    if("true".equalsIgnoreCase(attendance)){
                        studentPresence.add("Present");
                    }
                    else if("false".equalsIgnoreCase(attendance)){
                        studentPresence.add("Absent");
                    }
                    else if("late".equalsIgnoreCase(attendance)){
                        studentPresence.add("Late");
                    }
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
