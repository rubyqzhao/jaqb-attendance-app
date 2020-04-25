package com.example.jaqb.ui.instructor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.jaqb.R;
import com.example.jaqb.ui.LogoutActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activity to show the user a list of class dates based on the course code.
 * Gets the days listed in the database.
 *
 * @author Amanjot Singh
 * @version 1.0
 */
public class ClassDatesActivity extends LogoutActivity {

    private String courseCode;
    private List<String> courseDates;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private DatabaseReference databaseReference;

    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState    the previous state of the app
     */
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_calendar_view);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        databaseReference = FirebaseDatabase.getInstance().getReference("InstructorAttendance")
                .child(courseCode);
        courseDates = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = (String) keyNode.getKey();
                    courseDates.add(date);
                }
                // Attach to the activity
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();
                try {
                    setCustomResourceForDates();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String dateSelected = formatter.format(date);
                Toast.makeText(getApplicationContext(), dateSelected,
                        Toast.LENGTH_SHORT).show();
                if(courseDates.contains(dateSelected)) {
                    Intent intent = new Intent(getApplicationContext(), CheckAttendance.class);
                    intent.putExtra("date", dateSelected);
                    intent.putExtra("code", courseCode);

                    startActivity(intent);
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    /**
     * This method sets the custom dates and their color for the students in the
     * attendance history calendar
     *
     * @throws ParseException while parsing the string to date
     */
    private void setCustomResourceForDates() throws ParseException {
        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(Color.BLUE);
            for(String d : courseDates){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(d);
                caldroidFragment.setBackgroundDrawableForDate(blue, date);
                caldroidFragment.setTextColorForDate(R.color.caldroid_black, date);
            }
        }
    }
}
