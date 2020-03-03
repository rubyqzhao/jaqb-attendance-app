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
        new Badge(0, "First Check-in", R.drawable.star_disabled),
                new Badge(1, "Attended Your Classes", R.drawable.star_enabled),
                new Badge(2, "Five Day Streak!", R.drawable.star_enabled),
                new Badge(3, "Ten Day Streak!", R.drawable.star_disabled),
                new Badge(4, "Earned Three Badges", R.drawable.star_enabled),
                new Badge(5, "Perfect Attendance", R.drawable.star_disabled),
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
