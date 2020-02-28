package com.example.jaqb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.services.FireBaseDBServices;

/**
 * Activity class that acts as a landing page after the user logs in. It routes the user
 * to different activities based on their action.
 * */

public class CheckInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        Toolbar myToolbar = findViewById(R.id.checkin_toolbar);
        setSupportActionBar(myToolbar);
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
            FireBaseDBServices.getInstance().logoutUser();
            Intent intent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        public void checkinButtonOnClick(View view) {
        Intent intent = new Intent(this, QRCheckin.class);
        startActivity(intent);
    }

    public void setAlarmButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void seeRewardsButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void seeAttendanceButtonOnClick(View view) {
        Intent intent = new Intent(this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void myCoursesButtonOnClick(View view) {
        Intent intent = new Intent(this, MyCoursesActivity.class);
        startActivity(intent);
    }
}
