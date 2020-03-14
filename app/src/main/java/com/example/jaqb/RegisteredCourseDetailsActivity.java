package com.example.jaqb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.services.FireBaseDBServices;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author amanjotsingh
 *
 * This activity class displays the details of the course and helps user register
 * for that course.
 * */

public class RegisteredCourseDetailsActivity extends AppCompatActivity implements View.OnClickListener {

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
    private EditText alertMinutesText;
    private DialogInterface.OnClickListener dialogClickListener;
    private Button setAlarmButton, deleteAlarmButton;
    private FireBaseDBServices fireBaseDBServices;
    private LoggedInUser currentUser;
    private Course registerCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_course_details);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v)
    {
        addCalendarEvent(v);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addCalendarEvent(View view) {
        Calendar calendarEvent = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");

        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();

        long nextDay = getNextDay();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, courseName);
        values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        values.put(CalendarContract.Events.DTSTART, nextDay);
        values.put(CalendarContract.Events.DTEND, nextDay + 60 * 60 * 1000);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(timeZone));
        //values.put(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calendarEvent.getTimeInMillis()); // Only date part is considered when ALL_DAY is true; Same as DTSTART
            //.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calendarEvent.getTimeInMillis() + 60 * 60 * 1000) // Only date part is considered when ALL_DAY is true
            //.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location")
            //.putExtra(CalendarContract.Events.DESCRIPTION, "DESCRIPTION") // Description
            //.putExtra(Intent.EXTRA_EMAIL, currentUser.get)
        values.put(CalendarContract.Events.EXDATE, "");
        values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=4;BYDAY=" + courseDays); // Recurrence rule
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        if(view.equals(deleteAlarmButton))
            values.put(CalendarContract.Events.HAS_ALARM, 0);
        Uri event = cr.insert(EVENTS_URI, values);

        if(event != null) {
            if(view.equals(setAlarmButton)) {
                int alertMinutes = Integer.parseInt(String.valueOf(alertMinutesText.getText()));
                Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
                values = new ContentValues();
                values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
                values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                values.put(CalendarContract.Reminders.MINUTES, alertMinutes);
                cr.insert(REMINDERS_URI, values);
                Toast.makeText(this, "Reminder Created!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Reminder Removed!", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getNextDay()
    {
        String[] days = courseDays.split("[,]");
        long soonestDay = getTimeOfNextDayCode(days[0]);
        for(int i = 1; i < days.length; i++)
        {
            long nextDay = getTimeOfNextDayCode(days[i]);
            if(nextDay < soonestDay)
                soonestDay = nextDay;
        }
        return soonestDay;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getTimeOfNextDayCode(String dayCode)
    {
        DayOfWeek dayOfWeek;
        switch(dayCode)
        {
            case "MO":
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "TU":
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "WE":
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "TH":
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "FR":
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "SA":
                dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "SU":
                dayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                dayOfWeek = null;
        }
        return ZonedDateTime.now().with(TemporalAdjusters.nextOrSame(dayOfWeek)).toInstant().toEpochMilli();
    }

    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") :
                        Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }
}