package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.jaqb.MyCoursesActivity;
import com.example.jaqb.R;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.menu.MenuOptionsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeActivity extends MenuOptionsActivity {
    private FusedLocationProviderClient fusedLocationClient;
    //private double latitude;
    //private double longitude;
    //private TextView coordDisplay;
    private FireBaseDBServices fireBaseDBServices;
    private Course nextClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_classes);
        Toolbar myToolbar = findViewById(R.id.instructor_toolbar);
        setSupportActionBar(myToolbar);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //coordDisplay = findViewById(R.id.gps_coord);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        nextClass = new Course("SER 515", "Dummy Class");
    }

    protected void onResume() {
        super.onResume();
        //todo: update coordDisplay using what's stored in the database
    }

    public void GetQRButtonOnClick(View view) {
        int res = fireBaseDBServices.startAttendanceForCourse(nextClass);
        System.out.println("RESULT AFTER GENERATING ATTENDANCE: " + res);
        Intent intent = new Intent(this, DisplayQRCodeActivity.class);
        startActivity(intent);
    }

    /*public void submitLocationButtonOnClick(View view) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            coordDisplay.setText(latitude + ", " + longitude);
                        }
                    }
                });
    }*/

    public void myCoursesButtonOnClick(View view) {
        Intent intent = new Intent(this, MyCoursesActivity.class);
        startActivity(intent);
    }

    public void attendaceHistoryButtonOnClick(View view) {
        Intent intent = new Intent(this, AttendanceHistoryInstructorActivity.class);
        startActivity(intent);
    }
}
