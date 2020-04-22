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

public class AttendanceHistoryOptionsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_attendance_history_options);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        courseAttendance = new ArrayList<>();
        textView = (TextView) findViewById(R.id.attendance_history_text);
        textView.setText("Attendance History for : " + courseCode);
        historyByDates = (Button) findViewById(R.id.history_by_dates);
        historyByNames = (Button) findViewById(R.id.history_by_students);
        barChart = (BarChart) findViewById(R.id.barchart);

        dates = new ArrayList();

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
