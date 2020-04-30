package com.example.jaqb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.CalendarServices;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.ui.LogoutActivity;

/**
 * @author amanjotsingh
 *
 * This activity class displays the details of the course and helps user register
 * for that course.
 * */

public class RegisteredCourseDetailsActivity extends LogoutActivity implements View.OnClickListener {

    private String courseCode;
    private String courseName;
    private String courseDays;
    private String courseInstructor;
    private String time;
    private TextView courseCodeTextView;
    private TextView courseNameTextView;
    private TextView courseDaysTextView;
    private TextView courseInstructorTextView;
    private TextView courseLocation;
    private TextView courseTime;
    private TextView currentAlarm;
    private EditText alertMinutesText;
    private DialogInterface.OnClickListener dialogClickListener;
    private Button setAlarmButton, deleteAlarmButton;
    private FireBaseDBServices fireBaseDBServices;
    private LoggedInUser currentUser;
    private Course registerCourse;
    private int hashCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_registered_course_details);
        courseCode = (String) getIntent().getCharSequenceExtra("code");
        courseName = (String) getIntent().getCharSequenceExtra("name");
        courseDays = (String) getIntent().getCharSequenceExtra("days");
        courseInstructor = (String) getIntent().getCharSequenceExtra("instructor");
        time = (String) getIntent().getCharSequenceExtra("time");

        courseCodeTextView = (TextView) findViewById(R.id.code);
        courseNameTextView = (TextView) findViewById(R.id.name);
        courseDaysTextView = (TextView) findViewById(R.id.days);
        courseInstructorTextView = (TextView) findViewById(R.id.instructor);
        courseLocation = (TextView) findViewById(R.id.location);
        courseTime = (TextView) findViewById(R.id.time);
        alertMinutesText = (EditText) findViewById(R.id.numMinutes);
        currentAlarm = (TextView) findViewById(R.id.currentAlarm);

        hashCode = Math.abs((courseName + courseCode + courseDays + time).hashCode());

        courseCodeTextView.setText(courseCode);
        courseNameTextView.setText(courseName);
        courseDaysTextView.setText(courseDays);
        courseInstructorTextView.setText(courseInstructor);
        courseLocation.setText("Poly AGBC 150");
        courseTime.setText(time);

        registerCourse = new Course();
        registerCourse.setDays(courseDays);
        registerCourse.setCode(courseCode);
        registerCourse.setInstructorName(courseInstructor);
        registerCourse.setCourseName(courseName);
        registerCourse.setTime(time);

        fireBaseDBServices = FireBaseDBServices.getInstance();
        currentUser = fireBaseDBServices.getCurrentUser();


        setAlarmButton = (Button) findViewById(R.id.register);
        deleteAlarmButton = (Button) findViewById(R.id.buttonDeleteAlarm);
        setAlarmButton.setOnClickListener(this);
        deleteAlarmButton.setOnClickListener(this);

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // update database with this course for that user
                        int res = fireBaseDBServices.registerCourse(registerCourse, currentUser);
                        Intent intent = new Intent();
                        if(res == 1){
                            System.out.println("Registered in new course");
                            currentUser.updateCourse(registerCourse);
                            intent.setClass(getApplicationContext(), MyCoursesActivity.class);
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();

                        }
                        else if(res == 0){
                            System.out.println("Error in registering course");
                            intent.setClass(getApplicationContext(), CourseRegistrationActivity.class);
                            break;
                        };
                        startActivity(intent);
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            deleteAlarmButton.setEnabled(false);
            setAlarmButton.setEnabled(false);
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                    1);
        } else {
            // Permission has already been granted
            changeAlarm(getCurrentAlarm());
        }
    }

    private int getCurrentAlarm()
    {
        ContentResolver cr = getContentResolver();
        try {
            Cursor cursor = cr.query(Uri.parse(CalendarServices.getCalendarUriBase(true) + "reminders"),
                    new String[]{ CalendarContract.Reminders.MINUTES },
                    CalendarContract.Reminders.EVENT_ID + "=" + hashCode, null , null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    private void changeAlarm(int alarm) {
        if (alarm == -1)
            currentAlarm.setText("No Alarm Set");
        else
            currentAlarm.setText("Alarm set to " + alarm + " minute(s) before class");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v)
    {
            if(v.equals(setAlarmButton))
                addReminder();
            else if(v.equals(deleteAlarmButton))
                removeCalendarReminder();
    }

    public void removeCalendarReminder()
    {
        ContentResolver cr = getContentResolver();
        int numDeleted = cr.delete(Uri.parse(CalendarServices.getCalendarUriBase(true) + "reminders"),
           CalendarContract.Reminders.EVENT_ID + "=" + hashCode,
      null);
        if(numDeleted > 0) {
            Toast.makeText(this, "Reminder Removed!", Toast.LENGTH_SHORT).show();
            changeAlarm(-1);
        }
        else
            Toast.makeText(this, "Alarm is already not set!", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addReminder()
    {
        int alertMinutes = 0;
        try {
            alertMinutes = Integer.parseInt(String.valueOf(alertMinutesText.getText()));
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Must enter the number of minutes for a reminder!", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(Uri.parse(CalendarServices.getCalendarUriBase(true) + "events"),
                new String[]{ CalendarContract.Events._ID },
                CalendarContract.Events._ID + "=" + hashCode, null , null);
        cursor.moveToFirst();
        boolean result;
        if(cursor.getCount() > 0)
            result = editReminder(alertMinutes);
        else
            result = addCalendarEvent(alertMinutes);

        if(result) {
            Toast.makeText(this, "Reminder Created!", Toast.LENGTH_SHORT).show();
            changeAlarm(alertMinutes);
        }
        else
            Toast.makeText(this, "Error Creating Reminder", Toast.LENGTH_SHORT).show();
    }

    public boolean editReminder(int alertMinutes)
    {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Reminders.MINUTES, alertMinutes);
        int rows = cr.update(Uri.parse(CalendarServices.getCalendarUriBase(true) + "reminders"), values,
                CalendarContract.Reminders.EVENT_ID + "=" + hashCode,
                null);
        if(rows == 0)
            return createReminder(alertMinutes);
        return true;
    }

    public boolean createReminder(int alertMinutes)
    {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri REMINDERS_URI = Uri.parse(CalendarServices.getCalendarUriBase(true) + "reminders");
        values.put(CalendarContract.Reminders.EVENT_ID, hashCode);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, alertMinutes);
        cr.insert(REMINDERS_URI, values);
        return true;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean addCalendarEvent(int alertMinutes) {
        Calendar calendarEvent = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(CalendarServices.getCalendarUriBase(true) + "events");

        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();

        String timeSplit[] = time.split("[:]");
        if(timeSplit.length != 2)
            return false;

        int hour, minute;
        try {
            hour = Integer.parseInt(timeSplit[0]);
            minute = Integer.parseInt(timeSplit[1]);
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        long startTime = CalendarServices.getNextDaySemester(courseDays, currentUser)+ (hour * 60 + minute) * 60000;
        ContentValues values = new ContentValues();

        Uri event = null;
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events._ID, hashCode);
        values.put(CalendarContract.Events.TITLE, courseName);
        values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, startTime + 60 * 60 * 1000);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, currentUser.getSemester().getTimeZoneID());
        //values.put(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calendarEvent.getTimeInMillis()); // Only date part is considered when ALL_DAY is true; Same as DTSTART
            //.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calendarEvent.getTimeInMillis() + 60 * 60 * 1000) // Only date part is considered when ALL_DAY is true
            //.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location")
            //.putExtra(CalendarContract.Events.DESCRIPTION, "DESCRIPTION") // Description
            //.putExtra(Intent.EXTRA_EMAIL, currentUser.get)
        //values.put(CalendarContract.Events.EXDATE, currentUser.getSemester().getOffDaysFormatted());
        values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=" + courseDays + ";UNTIL=" + currentUser.getSemester().getEndSemesterDate()); // Recurrence rule
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        Log.i("VALUES", values.toString());
        event = cr.insert(EVENTS_URI, values);

        if(event != null) {
            Uri REMINDERS_URI = Uri.parse(CalendarServices.getCalendarUriBase(true) + "reminders");
            values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            values.put(CalendarContract.Reminders.MINUTES, alertMinutes);
            cr.insert(REMINDERS_URI, values);
            return true;
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    deleteAlarmButton.setEnabled(true);
                    setAlarmButton.setEnabled(true);
                    changeAlarm(getCurrentAlarm());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Needs Calendar Permission to set Alarm!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}