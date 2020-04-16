package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.R;

public class AttendanceHistoryOptionsActivity extends AppCompatActivity {

    private Button historyByDates;
    private Button historyByNames;
    private String courseCode;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history_options);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        textView = (TextView) findViewById(R.id.attendance_history_text);
        textView.setText("Attendance History for : " + courseCode);
        historyByDates = (Button) findViewById(R.id.history_by_dates);
        historyByNames = (Button) findViewById(R.id.history_by_students);

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
