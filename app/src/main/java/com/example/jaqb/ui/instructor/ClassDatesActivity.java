package com.example.jaqb.ui.instructor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to show the user a list of class dates based on the course code.
 * Gets the days listed in the database.
 *
 * @author Amanjot Singh
 * @version 1.0
 */
public class ClassDatesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String courseCode;
    private List<String> courseDates;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;

    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState    the previous state of the app
     */
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_dates);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        databaseReference = FirebaseDatabase.getInstance().getReference("InstructorAttendance")
                .child(courseCode);
        courseDates = new ArrayList<>();
        listView = (ListView) findViewById(R.id.dates_course_list);
        findViewById(R.id.dates_progressBar).setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.class_list_item,
                R.id.class_item_name, courseDates);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = (String) keyNode.getKey();
                    courseDates.add(date);
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(this);
    }

    /**
     * Determines the actions of the item shown on the page. When clicked,
     * the button will take the user to the check attendance activity.
     * @param parent    the parent adapterView object containing the course view
     * @param view      the course view being displayed
     * @param position  the current position of the item in the list
     * @param id        the id of the item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dateSelected = (String) arrayAdapter.getItem(position);
        Intent intent = new Intent(this, CheckAttendance.class);
        intent.putExtra("code", courseCode);
        intent.putExtra("date", dateSelected);
        startActivity(intent);
    }
}
