package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
import com.example.jaqb.ui.LogoutActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryOptionsActivity extends LogoutActivity {

    private Button historyByDates;
    private Button historyByNames;
    private String courseCode;
    private TextView textView;
    private BarChart barChart;
    private List<BarEntry> courseAttendance;
    private ArrayList dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_attendance_history_options);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        courseAttendance = new ArrayList<>();
        textView = (TextView) findViewById(R.id.attendance_history_text);
        textView.setText("Attendance History for : " + courseCode);
        historyByDates = (Button) findViewById(R.id.history_by_dates);
        historyByNames = (Button) findViewById(R.id.history_by_students);
        barChart = (BarChart) findViewById(R.id.barchart);

        dates = new ArrayList();

        /*For demo purpose
        courseAttendance.add(new BarEntry(1, 0));
        courseAttendance.add(new BarEntry(2, 1));
        courseAttendance.add(new BarEntry(3, 2));
        courseAttendance.add(new BarEntry(5, 3));
        courseAttendance.add(new BarEntry(10, 4));
        courseAttendance.add(new BarEntry(5, 5));
        courseAttendance.add(new BarEntry(8, 6));
        courseAttendance.add(new BarEntry(9, 7));
        courseAttendance.add(new BarEntry(4, 8));
        courseAttendance.add(new BarEntry(6, 9));

        dates.add("2008");
        dates.add("2009");
        dates.add("2010");
        dates.add("2011");
        dates.add("2012");
        dates.add("2013");
        dates.add("2014");
        dates.add("2015");
        dates.add("2016");
        dates.add("2017");
        */

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("InstructorAttendance").child(courseCode);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    dates.add((String) keyNode.getKey());
                    int countPresent = 0;
                    for(DataSnapshot node : keyNode.getChildren()){
                        String key = (String) node.getKey();
                        String value = (String) node.getValue();
                        if("true".equalsIgnoreCase(value) || "late".equalsIgnoreCase(value)){
                            countPresent++;
                        }
                    }
                    courseAttendance.add(new BarEntry(countPresent, i++));
                }
                BarDataSet bardataset = new BarDataSet(courseAttendance, "Students present");
                barChart.animateY(2000);
                bardataset.setColor(Color.GREEN);
                BarData data = new BarData(dates, bardataset);
                barChart.setData(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        historyByDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassDatesActivity.class);
                intent.putExtra("code", courseCode);
                startActivity(intent);
            }
        });

        historyByNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttendanceHistoryByNamesActivity.class);
                intent.putExtra("code", courseCode);
                startActivity(intent);
            }
        });
    }
}
