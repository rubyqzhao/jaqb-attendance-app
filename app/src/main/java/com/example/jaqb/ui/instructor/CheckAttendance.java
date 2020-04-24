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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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

        PieChart pieChart = findViewById(R.id.piechart);
        pieChart.setVisibility(View.VISIBLE);
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new Entry(945f, 0));
        NoOfEmp.add(new Entry(1040f, 1));
        NoOfEmp.add(new Entry(1133f, 2));
        NoOfEmp.add(new Entry(1240f, 3));
        NoOfEmp.add(new Entry(1369f, 4));
        NoOfEmp.add(new Entry(1487f, 5));
        NoOfEmp.add(new Entry(1501f, 6));
        NoOfEmp.add(new Entry(1645f, 7));
        NoOfEmp.add(new Entry(1578f, 8));
        NoOfEmp.add(new Entry(1695f, 9));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        PieData data = new PieData(year, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);
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
