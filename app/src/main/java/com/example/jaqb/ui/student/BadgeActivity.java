package com.example.jaqb.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Badge;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.ui.menu.MenuOptionsActivity;
import com.example.jaqb.services.FireBaseDBServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author rubyqzhao
 *
 * Activity class to depict a user's earned/unearned badge icons
 * */

public class BadgeActivity extends MenuOptionsActivity {
    private LoggedInUser currentUser;
    private FireBaseDBServices fireBaseDBServices;
    private DatabaseReference databaseReference;
    private int notEarnedIcon = R.drawable.mystery_badgexhdpi;
    private Badge[] badgeList = {
        new Badge(0, "perfectAttendance", R.drawable.all_classes_attended_2x),
        new Badge(1, "perfectAttendanceOneCourse", R.drawable.all_classes_of_a_course_2x),
        new Badge(2, "firstClass", notEarnedIcon),
        new Badge(3, "fiveStreak", notEarnedIcon)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        setContentView(R.layout.activity_badge);
        badgeList = updateBadges();
        Log.d("database", "onCreate");
        Log.d("database", Integer.toString(badgeList[3].getImage()));
        Log.d("database", Integer.toString(badgeList[2].getImage()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GridView gridView = findViewById(R.id.badge_grid);
        badgeList = updateBadges();
        BadgeAdapter adapter = new BadgeAdapter(this, badgeList);
        gridView.setAdapter(adapter);
        Log.d("database", "onStart");
        Log.d("database", Integer.toString(badgeList[3].getImage()));
        Log.d("database", Integer.toString(badgeList[2].getImage()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        GridView gridView = findViewById(R.id.badge_grid);
        badgeList = updateBadges();
        BadgeAdapter adapter = new BadgeAdapter(this, badgeList);
        gridView.setAdapter(adapter);
        Log.d("database", "OnResume");
        Log.d("database", Integer.toString(badgeList[3].getImage()));
        Log.d("database", Integer.toString(badgeList[2].getImage()));
    }

    private Badge[] updateBadges() {
        List<Badge> tempBadgeList = new ArrayList<>();
        HashMap<String, Boolean> isBadgeEarned;
        DatabaseReference statsRef = databaseReference.child(currentUser.getuID()).child("stats");
        statsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("database", "Value is " + keyNode.toString());
                    if(keyNode.getKey().equals("totalClassesOverall")) {
                        Log.d("database", "true");
                    }
                    else {
                        for(DataSnapshot stats : keyNode.getChildren()) {
                            if(stats.getKey().equals("currentStreak")) {
                                if(Integer.parseInt(stats.getValue().toString()) >= 5) {
                                    badgeList[3].setImage(R.drawable.attend_first_class_2x);
                                }
                            }
                            else if(stats.getKey().equals("totalClasses")) {
                                if(Integer.parseInt(stats.getValue().toString()) > 0) {
                                    badgeList[2].setImage(R.drawable.go_5_days_a_week_2x);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return badgeList;
    }

}
