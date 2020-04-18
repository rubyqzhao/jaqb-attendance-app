package com.example.jaqb.ui.student;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.jaqb.R;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;
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
 * Class to hold the activity that shows the attendance history of an user
 */
public class AttendanceHistoryStudentActivity extends AppCompatActivity {

    private String courseCode;
    private DatabaseReference databaseReference;
    private LoggedInUser currentUser;
    private FireBaseDBServices fireBaseDBServices;
    private List<String> courseAttendance;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState the previous state of app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        courseAttendance = new ArrayList<>();
        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(currentUser.getuID())
                .child("attendanceHistory")
                .child(courseCode);
        databaseReference.addValueEventListener(new ValueEventListener() {
            /**
             * Triggers when there has to a change in Database
             * @param dataSnapshot the current state of database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String date = keyNode.getKey();
                    String presence = (String) keyNode.getValue();
                    courseAttendance.add(date + " : " + presence);
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

            /**
             * Triggers if data fails to get updated
             * @param databaseError the error due to which change could not happen
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

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
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
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
            ColorDrawable red = new ColorDrawable(Color.RED);
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            ColorDrawable yellow = new ColorDrawable(Color.YELLOW);
            for(String s : courseAttendance){
                String[] data = s.split(":");
                String tempDate = data[0].trim();
                String dateColor = data[1].trim();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tempDate);
                if(dateColor.equalsIgnoreCase("true")){
                    caldroidFragment.setBackgroundDrawableForDate(green, date);
                    caldroidFragment.setTextColorForDate(R.color.caldroid_black, date);
                }
                else if(dateColor.equalsIgnoreCase("false")){
                    caldroidFragment.setBackgroundDrawableForDate(red, date);
                    caldroidFragment.setTextColorForDate(R.color.caldroid_black, date);
                }
                else if(dateColor.equalsIgnoreCase("late")){
                    caldroidFragment.setBackgroundDrawableForDate(yellow, date);
                    caldroidFragment.setTextColorForDate(R.color.caldroid_black, date);
                }
            }
        }
    }
}
