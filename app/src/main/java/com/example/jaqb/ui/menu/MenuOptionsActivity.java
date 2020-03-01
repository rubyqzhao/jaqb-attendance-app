package com.example.jaqb.ui.menu;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.CourseRegistrationActivity;
import com.example.jaqb.MainActivity;
import com.example.jaqb.R;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.instructor.HomeActivity;

public class MenuOptionsActivity extends AppCompatActivity {
    private FireBaseDBServices dbServices;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        dbServices = FireBaseDBServices.getInstance();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            dbServices.logoutUser();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_instructor) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
