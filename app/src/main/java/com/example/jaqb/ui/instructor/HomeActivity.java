package com.example.jaqb.ui.instructor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jaqb.MyCoursesActivity;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.services.InstructorServicesHelper;
import com.example.jaqb.ui.menu.MenuOptionsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.concurrent.TimeUnit;

/**
 * The activity for the landing page of the instructor side of the app. The
 * user will arrive at this page when they first log in as an instructor.
 * They have the options to perform various actions using the buttons displayed
 * on the page, including generating QR codes and viewing attendance history.
 *
 * @author Ruby Zhao
 * @author Amanjot Singh
 * @version 1.0
 */
public class HomeActivity extends MenuOptionsActivity {
    private FusedLocationProviderClient fusedLocationClient;
    //private double latitude;
    //private double longitude;
    //private TextView coordDisplay;
    private FireBaseDBServices fireBaseDBServices;
    private Course nextClass;

    /**
     * Triggers when the user first opens the page. Initializes values and sets
     * the values for the activity view.
     * @param savedInstanceState    the previous state of the app
     */
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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    /**
     * Triggers when the user returns to the page.
     */
    protected void onResume() {
        super.onResume();
        //todo: update coordDisplay using what's stored in the database
    }
  
    public void GetQRButtonOnClick(View view) {
        int res = fireBaseDBServices.startAttendanceForCourse(nextClass);
        Intent intent = new Intent();
        intent.setClass(this, DisplayQRCodeActivity.class);
        // generate random code
        InstructorServicesHelper instructorServicesHelper = new InstructorServicesHelper();
//        boolean generateCode = instructorServicesHelper.isPreviousCodeValid(nextClass.getCourseQRCode(), TimeUnit.HOURS);
        boolean isPrevCodeValid = instructorServicesHelper.isPreviousCodeValid("5115 2020-04-05 16:04:15", TimeUnit.HOURS);
        int code = 0;
        if(!isPrevCodeValid){
            code = instructorServicesHelper.generateRandomCode();
            intent.putExtra("validCode", false);
        }
        else{
//            code = Integer.getInteger(nextClass.getCourseQRCode().split(" ")[0]);
            code = Integer.valueOf("5115");
            intent.putExtra("validCode", true);
        }
        intent.putExtra("code", code);
        startActivity(intent);
    }

    /**
     * Triggers an action to get the user's current location when clicking the
     * Submit Location button.
     * @param view  the current app view
     */
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

    /**
     * Triggers an action to direct the user to the course listing page when clicking the
     * My Courses button.
     * @param view  the current app view
     */
    public void myCoursesButtonOnClick(View view) {
        Intent intent = new Intent(this, MyCoursesActivity.class);
        startActivity(intent);
    }

    /**
     * Triggers an action to direct the user to the attendance history page when clicking
     * the Attendance History button.
     * @param view  the current app view
     */
    public void attendaceHistoryButtonOnClick(View view) {
        Intent intent = new Intent(this, AttendanceHistoryInstructorActivity.class);
        startActivity(intent);
    }
}
