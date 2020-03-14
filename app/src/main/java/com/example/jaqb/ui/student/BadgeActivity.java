package com.example.jaqb.ui.student;

import android.os.Bundle;
import android.widget.GridView;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Badge;
import com.example.jaqb.ui.menu.MenuOptionsActivity;

/**
 * @author rubyqzhao
 *
 * Activity class to depict a user's earned/unearned badge icons
 * */

public class BadgeActivity extends MenuOptionsActivity {

    //todo: replace placeholder badges with the designed badge icons
    private Badge[] badges = {
        new Badge(0, "All Classes Attended", R.drawable.all_classes_attended_2x),
                new Badge(1, "All Classes in a Course", R.drawable.all_classes_of_a_course_2x),
                new Badge(2, "Attend First Class", R.drawable.attend_first_class_2x),
                new Badge(3, "Five Day Streak", R.drawable.go_5_days_a_week_2x)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        GridView gridView = findViewById(R.id.badge_grid);
        BadgeAdapter adapter = new BadgeAdapter(this, this.badges);
        gridView.setAdapter(adapter);
    }


}
