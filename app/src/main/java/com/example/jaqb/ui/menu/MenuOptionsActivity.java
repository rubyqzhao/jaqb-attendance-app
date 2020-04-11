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

/**
 * Class to hold the menu view
 */

public class MenuOptionsActivity extends AppCompatActivity {
    private FireBaseDBServices dbServices;

    /**
     * This public triggers when menu is created
     * @param menu f type MENU
     * @return boolean value if the menu is created or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        dbServices = FireBaseDBServices.getInstance();
        return true;
    }

    /**
     * This public function checks which option is selected and launch selected activity
     * @param item of type MenuItem
     * @return boolean based on if the activity is launched
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            dbServices.logoutUser();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
