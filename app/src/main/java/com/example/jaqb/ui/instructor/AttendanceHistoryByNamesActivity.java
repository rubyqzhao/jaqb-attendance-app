package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
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

    private String courseCode;
    private String date;
    private List<RegisteredUser> studentsAttendance;
    private List<String> studentIds;
    private List<String> studentPresence;
    private List<String> studentNames;
    private Map<String, RegisteredUser> studentData;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private Query databaseReference;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Course")
                .orderByChild("code").equalTo(courseCode);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = arrayAdapter.getItem(position);
                String item_id = item.split(":")[1].trim();
                Intent intent = new Intent(getApplicationContext(), AttendanceHistoryIndividualStudentActivity.class);
                intent.putExtra("studentId", item_id);
                intent.putExtra("code", courseCode);
                startActivity(intent);
            }
        });
    }

    private void getDisplayDate(){
        for(String id : studentIds){
            if(studentData.containsKey(id)){
                RegisteredUser user = studentData.get(id);
                studentNames.add(user.getfName() + " " + user.getlName() + " ID : " + id);
            }
        }
    }
}
