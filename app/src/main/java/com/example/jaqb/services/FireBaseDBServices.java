package com.example.jaqb.services;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Badge;
import com.example.jaqb.data.model.Course;
import com.example.jaqb.data.model.SemesterDate;
import com.example.jaqb.data.model.LoggedInUser;
import com.example.jaqb.data.model.RegisteredUser;
import com.example.jaqb.data.model.Semester;
import com.example.jaqb.data.model.User;
import com.example.jaqb.data.model.UserLevel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

/**
 * @author amanjotsingh
 * @author jkdrumm
 *
 * This class contains the methods required to communicate with the database
 * */

public class FireBaseDBServices {

    private static final String TAG = "EmailPassword";
    private static final FireBaseDBServices dbService = new FireBaseDBServices();

    private FirebaseAuth mAuth;
    private LoggedInUser currentUser;
    private FirebaseDatabase database;
    private List<Course> allCourses = new ArrayList<>();

    public List<Course> getAllCourses() {
        return allCourses;
    }

    private FireBaseDBServices() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public static FireBaseDBServices getInstance() {
        return dbService;
    }

    public void registerUser(final User newUser, final Observer observer) {
        mAuth.createUserWithEmailAndPassword(newUser.getUserName(), newUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            RegisteredUser registeredUser =
                                    new RegisteredUser(newUser.getFirstName(), newUser.getLastName());
                            DatabaseReference reff = database.getReference("User").child(firebaseUser.getUid());
                            reff.child("fname").setValue(registeredUser.getfName());
                            reff.child("lname").setValue(registeredUser.getlName());
                            reff.child("level").setValue(registeredUser.getLevel());
                            observer.update(null, registeredUser);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            observer.update(null, null);
                        }
                    }
                });
    }

    public void loginUser(String email, String password, final Observer observer) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            database.getReference("User").child(firebaseUser.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String fname = (String) dataSnapshot.child("fname").getValue();
                                            String lname = (String) dataSnapshot.child("lname").getValue();
                                            UserLevel level = UserLevel.valueOf((String) dataSnapshot.child("level").getValue());
                                            //dataSnapshot.child("courses").getChildren().iterator().next().getKey();
                                            List<String> courses = new ArrayList<>();
                                            for (DataSnapshot s : dataSnapshot.child("courses").getChildren()) {
                                                courses.add(s.getKey());
                                            }
                                            RegisteredUser registeredUser = new RegisteredUser(fname, lname, level, courses);
                                            currentUser = new LoggedInUser(firebaseUser, registeredUser);

                                            database.getReference("Course")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                                Course course = keyNode.getValue(Course.class);
                                                                allCourses.add(course);
                                                            }
                                            database.getReference("SemesterInfo")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String timeZone = (String) dataSnapshot.child("TimeZone").getValue();
                                                            SemesterDate startSemesterDate = new SemesterDate(dataSnapshot.child("StartDate").getValue(String.class));
                                                            SemesterDate endSemesterDate = new SemesterDate(dataSnapshot.child("EndDate").getValue(String.class));
                                                            SemesterDate[] offDays = new SemesterDate[(int) dataSnapshot.child("Offdays").getChildrenCount()];
                                                            int i = 0;
                                                            for (DataSnapshot keyNode : dataSnapshot.child("Offdays").getChildren()) {
                                                                String date = keyNode.getKey();
                                                                offDays[i] = new SemesterDate(date);
                                                                i++;
                                                            }
                                                            currentUser.setSemester(new Semester(timeZone, startSemesterDate, endSemesterDate, offDays));
                                                        }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            currentUser.setRegisteredCourses(getUserCourses(currentUser, allCourses));
                                            observer.update(currentUser, currentUser.getLevel());
                                            getBadges();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            observer.update(null, null);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            observer.update(null, null);
                        }
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
    }

    public LoggedInUser getCurrentUser() {
        return currentUser;
    }

    public void seeIfStillLoggedIn(final Context context) {
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println("Logged-in");
            database.getReference("User").child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fname = (String) dataSnapshot.child("fname").getValue();
                            String lname = (String) dataSnapshot.child("lname").getValue();
                            UserLevel level = UserLevel.valueOf((String) dataSnapshot.child("level").getValue());
                            List<String> courses = new ArrayList<>();
                            for (DataSnapshot s : dataSnapshot.child("courses").getChildren()) {
                                courses.add(s.getKey());
                            }
                            RegisteredUser registeredUser = new RegisteredUser(fname, lname, level, courses);
                            currentUser = new LoggedInUser(firebaseUser, registeredUser);
                            System.out.println(currentUser.getDisplayName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else
            System.out.println("Not Logged-in");
        //goToUserHomepage(context);
    }

    public int registerCourse(final Course newCourse, final LoggedInUser user) {
        int res = 0;
        try {
            DatabaseReference reff = database.getReference("User").child(user.getuID());
            reff.child("courses").child(newCourse.getCode()).setValue("true");
            boolean isStudent = "STUDENT" == user.getLevel().toString();
            if(isStudent) {
                Query query = database.getReference("Course/").orderByChild("code")
                        .equalTo(newCourse.getCode());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            Course course = keyNode.getValue(Course.class);
                            if(newCourse.getCode().equalsIgnoreCase(course.getCode())) {
                                database.getReference("Course").child(keyNode.getKey())
                                        .child("students").child(user.getuID()).setValue("true");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Course> getUserCourses(LoggedInUser currentUser, List<Course> allCourses) {
        List<String> courseNames = new ArrayList<>();
        courseNames.addAll(currentUser.getCourseNames());
        List<Course> userCourses = new ArrayList<>();
        for (String course : courseNames) {
            for (Course c : allCourses) {
                if (course.equalsIgnoreCase(c.getCode())) {
                    userCourses.add(c);
                }
            }
        }
        return userCourses;
    }

    public boolean courseAlreadyRegistered(String code) {
        List<String> presentCourses = currentUser.getCourseNames();
        for (String course : presentCourses) {
            if (code.equalsIgnoreCase(course)) {
                return true;
            }
        }
        return false;
    }

    public void logoutUser() {
        currentUser = null;
        mAuth.signOut();
        allCourses.clear();
    }

    public void addPoints(final int numPoints, final Observer observer) {
        final DatabaseReference reff = database.getReference("User").child(currentUser.getuID()).child("points");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try
                    {
                        long totalPoints = (long) dataSnapshot.getValue();
                        totalPoints += numPoints;
                        reff.setValue(totalPoints);
                    }
                    catch (NullPointerException e)
                    {
                        reff.setValue(numPoints);
                    }
                    observer.update(null, numPoints);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    observer.update(null, 0);
                }

            });
    }

    public void getBadges() {
        final int notEarnedIcon = R.drawable.mystery_badgexhdpi;
        DatabaseReference statsRef = database.getReference("User").child(currentUser.getuID()).child("stats");
        statsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Badge> badgeList = new ArrayList<>();
                badgeList.add(new Badge(0, "firstClass", notEarnedIcon));
                badgeList.add(new Badge(1, "fiveStreak", notEarnedIcon));
                badgeList.add(new Badge(2, "perfectAttendance", notEarnedIcon));
                badgeList.add(new Badge(3, "perfectAttendanceOneCourse", notEarnedIcon));

                int allClassTotal = 0;
                int allClassAttended = 0;

                for(DataSnapshot course : dataSnapshot.getChildren()) {
                    int classesAttended = 0;
                    int totalClasses = 0;

                    for(DataSnapshot stats : course.getChildren()) {
                        if (stats.getKey().equals("currentStreak")) {
                            //determine whether five streak achieved
                            Log.d("database streak", stats.getValue().toString());
                            if (Integer.parseInt(stats.getValue().toString()) >= 5)
                                badgeList.get(1).setImage(R.drawable.go_5_days_a_week_2x);
                        } else if (stats.getKey().equals("numAttended")) {
                            //determine whether first class was attended
                            classesAttended = Integer.parseInt(stats.getValue().toString());
                            allClassAttended += classesAttended;

                            Log.d("database numAttended", stats.getValue().toString());
                            if (classesAttended > 0)
                                badgeList.get(0).setImage(R.drawable.attend_first_class_2x);
                        } else if (stats.getKey().equals("totalClasses")) {
                            //get the sum of attendance in each class
                            totalClasses = Integer.parseInt(stats.getValue().toString());
                            allClassTotal += totalClasses;
                        }

                        //determine whether perfect attendance in this class
                        Log.d("database one class", classesAttended + " " + totalClasses);
                        if (classesAttended == 0 && totalClasses == 0) {}
                        else if (classesAttended == totalClasses) {
                            badgeList.get(3).setImage(R.drawable.all_classes_of_a_course_2x);
                        }
                    }
                }

                //determine whether perfect attendance for all classes
                Log.d("database all classes", allClassAttended + " " + allClassTotal);
                if(allClassAttended == allClassTotal) {
                    badgeList.get(2).setImage(R.drawable.all_classes_attended_2x);
                }

                Log.d("database", badgeList.toString());
                currentUser.setBadges(badgeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * @param nextClass
     * @return
     */
    public int startAttendanceForCourse(final Course nextClass) {
        final int[] attendanceCreated = {0};
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String strDate= formatter.format(date);
        System.out.println("THE DATE IS : " + strDate);
        try{
            final DatabaseReference reff = database.getReference("InstructorAttendance")
                    .child(nextClass.getCode()).child(strDate);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        System.out.println("IT EXISTS");
                        attendanceCreated[0] = 0;
                    }
                    else{
                        System.out.println("IT DOESN'T EXISTS");
                        Query query = database.getReference("Course").orderByChild("code")
                                .equalTo(nextClass.getCode());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = "";
                                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                                    key = keyNode.getKey();
                                    System.out.println("KEY IS : " + key);
                                }
                                database.getReference("Course").child(key).child("students")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                                                    String studentKey = keyNode.getKey();
                                                    System.out.println("STUDENT KEYS: " + studentKey);
                                                    database.getReference("InstructorAttendance")
                                                            .child(nextClass.getCode())
                                                            .child(strDate)
                                                            .child(studentKey).setValue(false);
                                                    database.getReference("User")
                                                            .child(studentKey)
                                                            .child("attendanceHistory")
                                                            .child(nextClass.getCode())
                                                            .child(strDate).setValue(false);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        attendanceCreated[0] = 1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return attendanceCreated[0];
    }

}
