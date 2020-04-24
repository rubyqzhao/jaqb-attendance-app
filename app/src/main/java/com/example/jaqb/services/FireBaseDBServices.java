package com.example.jaqb.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private DatabaseReference databaseReference;
    private List<Course> allCourses = new ArrayList<>();

    /**
     * @return list of courses for the logged-in user
     */
    public List<Course> getAllCourses() {
        return allCourses;
    }

    /**
     * Private constructor to initialize the authentication and database
     */
    private FireBaseDBServices() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    /**
     * @return singleton instance of the FireBaseDBServices class
     */
    public static FireBaseDBServices getInstance() {
        return dbService;
    }

//    public static DatabaseReference getReference() {return dbService.reference; }

    /**
     * This method registers a user in the firebase database as a student
     *
     * @param newUser User object for the new user registering for the application
     * @param observer Event listener for the success or failure to register the user
     */
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

    /**
     * This method logs-in the user into the application
     *
     * @param email email id used for login
     * @param password password entered by the user
     * @param observer Event listener for successful/failure in logging in
     */
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
                                                            observer.update(currentUser, currentUser.getLevel());
                                                        }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            currentUser.setRegisteredCourses(getUserCourses(currentUser, allCourses));
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

    /**
     * @return Object of LoggedInUser with the details of the user logged in
     */
    public LoggedInUser getCurrentUser() {
        return currentUser;
    }

    /**
     * This method checks if the user is still logged in and the session is valid
     *
     * @param context Current context of the application
     */
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

    /**
     * This method registers the user for a course
     *
     * @param newCourse course that user wants to register
     * @param user the user who is accessing the application
     * @return 1 for successful registration and 0 for failure
     */
    public int registerCourse(final Course newCourse, final LoggedInUser user) {
        int res = 0;
        try {
            DatabaseReference reff = database.getReference("User").child(user.getuID());
            reff.child("courses").child(newCourse.getCode()).setValue("true");
            boolean isStudent = "STUDENT" == user.getLevel().toString();
            boolean isInstructor = "INSTRUCTOR" == user.getLevel().toString();
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
            } else if(isInstructor) {
                Query query = database.getReference("Course").orderByChild("code")
                        .equalTo(newCourse.getCode());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                            String key = "";
                            key = keyNode.getKey();
                            database.getReference("Course")
                                    .child(key)
                                    .child("instructorName")
                                    .setValue(currentUser.getfName() + " " + currentUser.getlName());
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

    /**
     * This method returns the courses that the user is registered for
     *
     * @param currentUser the user who is logged in
     * @param allCourses list of all courses in the database
     * @return list of courses the user is registered for
     */
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

    /**
     * This method checks if the user is already registered in a course
     *
     * @param code the course code to be matched
     * @return true if the user is registered and false if the user is not registered
     */
    public boolean courseAlreadyRegistered(String code) {
        List<String> presentCourses = currentUser.getCourseNames();
        for (String course : presentCourses) {
            if (code.equalsIgnoreCase(course)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method clears the session information and logs out the current user
     */
    public void logoutUser() {
        currentUser = null;
        mAuth.signOut();
        allCourses.clear();
    }

    /**
     * This method adds the badges earned by the user from the database and sets up the
     * badges in the LoggedInUser object for this class
     */
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
     * This method sets up the database for taking attendance for a class. It is executed
     * when an instructor clicks on generate QR code button
     *
     * @author amanjotsingh
     *
     * @param nextClass this is the immediate next class that the instructor will be taking
     * @return 1 if the database setup is successful, 0 is there is failure in setting up
     *          the database
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
                                                            .child(studentKey).setValue("false");
                                                    database.getReference("User")
                                                            .child(studentKey)
                                                            .child("attendanceHistory")
                                                            .child(nextClass.getCode())
                                                            .child(strDate).setValue("false");
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


                      
                      
    /**
     * This method updates the QR code generated by the instructor. The QR code is stored along
     * with the timestamp in order to be able to check the 24 hour expiry time of the generated
     * code
     *
     * @param code code to be updated in the database
     * @param courseCode course for which the QR code needs to be stored
     */
    public void updateQRCode(final int code, String courseCode) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String strDate= formatter.format(date);
        try {
            Query query = database.getReference("Course").orderByChild("code")
                    .equalTo(courseCode);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String key = "";
                    for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        key = keyNode.getKey();
                        database.getReference("Course")
                                .child(key)
                                .child("courseQRCode")
                                .setValue(String.valueOf(code) + " " + strDate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
