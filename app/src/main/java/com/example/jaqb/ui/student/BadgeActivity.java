package com.example.jaqb.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Badge;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.ui.LogoutActivity;
import com.example.jaqb.ui.menu.MenuOptionsActivity;
import com.example.jaqb.services.FireBaseDBServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * @author rubyqzhao
 *
 * Activity class to depict a user's earned/unearned badge icons
 * */

public class BadgeActivity extends LogoutActivity {
    private LoggedInUser currentUser;
    private FireBaseDBServices fireBaseDBServices;
    private DatabaseReference databaseReference;


    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState the previous state of app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        onCreate(R.layout.activity_badge);
        GridView gridView = findViewById(R.id.badge_grid);
        BadgeAdapter adapter = new BadgeAdapter(this, currentUser.getBadges());
        gridView.setAdapter(adapter);
    }

}
