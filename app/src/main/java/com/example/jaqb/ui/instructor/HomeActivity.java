package com.example.jaqb.ui.instructor;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jaqb.CourseRegistrationActivity;
import com.example.jaqb.IncompleteActivity;
import com.example.jaqb.MainActivity;
import com.example.jaqb.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private TextView coordDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_classes);
        Toolbar myToolbar = findViewById(R.id.instructor_toolbar);
        setSupportActionBar(myToolbar);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        coordDisplay = findViewById(R.id.gps_coord);
    }

    protected void onResume() {
        super.onResume();
        //todo: update coordDisplay using what's stored in the database
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //todo: remove session information
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_classes) {
            Intent intent = new Intent(this, CourseRegistrationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SetRewardsButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void GetQRButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void submitLocationButtonOnClick(View view) {
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
    }
}
