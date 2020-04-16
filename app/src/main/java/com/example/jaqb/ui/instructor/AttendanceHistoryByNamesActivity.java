package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.R;

import java.util.HashMap;
import java.util.Map;

public class AttendanceHistoryByNamesActivity extends AppCompatActivity {

    private Map<String, String> studentMap;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_class_dates);
        studentMap = new HashMap<String, String>();
    }
}
