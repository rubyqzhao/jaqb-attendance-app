package com.example.jaqb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.jaqb.MainActivity;
import com.example.jaqb.R;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.menu.MenuOptionsActivity;

public class LogoutActivity extends MenuOptionsActivity {

    /**
     * Triggers when the user first accesses the activity
     * Sets up the view and logout bar
     * @param view the R.id view
     */
    protected void onCreate(int view) {
        setContentView(view);
        Toolbar myToolbar = findViewById(R.id.checkin_toolbar);
        setSupportActionBar(myToolbar);
    }

    /**
     * When the menu is accessed
     * @param menu f type MENU
     * @return true if menu loads, else false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    /**
     * Triggers when an option is selected from the list of options
     * @param item of type MenuItem
     * @return true if option gets selected
     */
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
}
