var express = require('express');
var router = express.Router();
var firebase = require("firebase/app");
require("firebase/database");
require('dotenv').config();
const multer = require('multer');
const csv = require('fast-csv');
const upload = multer({ dest: 'test/' });
const http = require('http');
const fs = require('fs');
var path = require('path');
require('firebase/auth');
var session = require('express-session');
var Chart = require('chart.js');

const firebaseConfig = {
    apiKey: "AIzaSyDxSA3WWlv6kQnBEiXiw6k8hmmlIztgHjY",
    authDomain: "jaqb-attendance-app.firebaseapp.com",
    databaseURL: "https://jaqb-attendance-app.firebaseio.com",
    projectId: "jaqb-attendance-app",
    storageBucket: "jaqb-attendance-app.appspot.com",
    messagingSenderId: "986621442209",
    appId: "1:986621442209:web:bb66e7ef1c1d9f962518e0",
    measurementId: "G-0RMT5V21FJ"
  };


// Initialize Firebase
firebase.initializeApp(firebaseConfig);
var database = firebase.database();

/* GET home page. */

function checkIfAdmin(req) {
    var loggingUserId = firebase.auth().currentUser.uid;
    var ref = database.ref('User/'+loggingUserId+'/level');
    ref.on("value", function(snapshot) {
        if(snapshot.val() === "ADMIN"){
            req.session.userLoggedIn = true;
            res.redirect('/home');
        }
    }, function(error) {
        console.log("error"+error.code);
    });
}

function checkAuth(req, res, next) {
    if (req.session.userLoggedIn) {
        next();
    } else {
        res.status(403).send('Unauthorized! Only Admins can login. <a href="/">Go back home</a>');
        return;
    }
  }

router.use(session({
    secret: 'jaqb-app',
    saveUninitialized: true,
    resave: true
}));

router.get('/', function(req, res) {
    res.render('login', {title: "login"});
});

router.post('/login', function(req, res) {
    var username = req.body.uname;
    var password = req.body.psw;
    console.log(username+" "+password);   
    firebase.auth().signInWithEmailAndPassword(username, password)
    .then(function(){
        checkIfAdmin(req);
    })
    .catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        res.render('error', {message: "Sign-in Error: "+errorCode+" "+ errorMessage});
      });
});

router.get('/signout', function(req, res) {
    firebase.auth().signOut().then(function() {
        req.session.userLoggedIn = false;
        res.redirect('/');
      }).catch(function(error) {
        res.render('error', {message: "Sign-out Error: "+errorCode+" "+ errorMessage});
      });      
});

router.use('/home', checkAuth);

router.get('/home', function(req, res) {
    res.render('home', {title: "Home"});
});

router.get('/about', function(req, res) {
    res.render('about', {title: "About"});
});

router.get('/courses', function(req, res) {
    getCourses(function(courseList) {
        res.render('index', {
            title: 'JAQB Admin',
            courses: courseList
        });
    });
});

router.use('/add_course', checkAuth);

router.get('/add_course', function(req, res) {
    res.render('add_course', {title: "Add Courses"});
});


// opens the page with list of users, enables admin to change user privileges
router.use('/user_privileges_page', checkAuth);

router.get('/user_privileges_page', function(req, res) {
    getUsers(function(userList) {
        res.render('user_privileges', {
            title: 'User Privileges',
            users: userList
        });
    });
});

// opens list of instructors in the app, enables admin to assign courses to them
router.use('/assign_courses', checkAuth);

router.get('/assign_courses', function(req, res) {
    getInstructors(function(instructorList) {
        res.render('all_instructors', {
            title: 'Instructors',
            instructors: instructorList
        });
    });
});

// get courses in page - add courses to the instructor
router.use('/all_courses', checkAuth);

router.get('/all_courses', function(req, res) {
    var ins_details = req.query.ins_data.split(',');
    var fname = ins_details[0];
    var lname = ins_details[1];
    getCoursesForInstructor(req.query.ins_data, function(available_courses){
        res.render('assign_courses', {
            title: fname + " " + lname,
            available_courses: available_courses
        });
    });
});

// post request to update the user privileges
router.post('/change_privilege', function(req, res) {
    changePrivilege(JSON.stringify(req.body));
    res.redirect('/user_priviliges');
});

router.post('/add_course_to_instructor', function(req, res) {
    //console.log(JSON.stringify(req.body) + " : BODY");
    addCourseToInstructor(JSON.stringify(req.body));
    res.redirect('/assign_courses');
});

router.post('/add-course', function (req, res) {
    var response = req.body;
    console.log(req.body);
    console.log(response);
    //if input isn't entered or invalid, prevent send
    if(response == null || response.code == null || response.name == null
        || response.days == null || response.instructor == null || response.time == null) {
            res.status(400).send('Invalid Submission! <a href="/add_course">Go back</a>');
    }
    else if(response.code.localeCompare("") == 0 || response.name.localeCompare("") == 0
        || response.days == [""] || response.instructor.localeCompare("") == 0
        || response.time.localeCompare("") == 0) {
        res.status(400).send('Information missing! <a href="/add_course">Go back</a>');
    }
    else {
        var daysList;
        if(response.days[0].length == 1) {
            daysList = response.days;
        }
        else {
            var index = 1;
            daysList = response.days[0];
            while (index < response.days.length) {
                daysList += "," + response.days[index];
                index++;
            }
        }
        console.log(daysList);
        database.ref('Course/').push().set({
            code: response.code,
            courseName: response.name,
            days: daysList,
            instructorName: response.instructor,
            time: response.time
        });
        res.status(200).send('Submitted! <a href="/add_course">Go back</a>');
    }
});

router.post('/delete-course', function(req, res) {
    var contents = JSON.stringify(req.body);
    var arr = contents.split(',');
    var courseCode = arr[0].substring(2);

    var userQuery = database.ref('Course/').orderByChild("code").equalTo(courseCode);
    userQuery.once('value', function (data) {
        if (!data.exists()) {
            return;
        }
        else {
            data.forEach(function (user) {
                database.ref('Course/' + user.key).remove()
                    .then(function () {
                        return;
                    })
                    .catch(function (error) {
                        return;
                    });
            });
        }
    });

    res.redirect('/index');
});


function getCourses(callback) {
    var courseRef = database.ref('Course/');
    courseRef.once('value', function(snapshot) {
        var courseList = [];

        snapshot.forEach(function(item) {
            var code = item.val().code;
            var name = item.val().courseName;
            var days = item.val().days;
            var time = item.val().time;
            var instructor = item.val().instructorName;
            var listing = [code, name, days, time, instructor];
            courseList.push(listing);
        });
        return callback(courseList);
    });
}

function addCourseToInstructor(request_body){
    var regex = new RegExp(/"|:|{|}/gi);
    var str1 = request_body.replace(regex, '');
    var details = str1.split(',');
    var course_code = details[0];
    var fname = details[1].split(' ')[0];
    var lname = details[1].split(' ')[1];
    console.log(course_code + "  --  " + fname + "  --  " + lname);
    var userQry = database.ref('User/').orderByChild("fname").equalTo(fname);
    userQry.once('value', function (data) {
        if (!data.exists()) {
            //res.send('No users found')
        }
        else {
            data.forEach(function (user) {
                database.ref('User/' + user.key + '/courses/' + course_code).set("true" )
                    .then(function () {
                        return;
                    })
                    .catch(function (error) {
                        return;
                    });
            });
        }
    });

    // updating the instructor name in the Course table for that course
    var courseQuery = database.ref('Course/').orderByChild('code').equalTo(course_code);
    courseQuery.once('value', function(data){
        data.forEach(function (user){
            var oldInstructor = user.val().instructorName.split(' ');
            console.log("PREVIOUS INSTRUCTOR NAME : " + oldInstructor);
            database.ref('Course/' + user.key + '/instructorName').set(fname + " " + lname)
                .then(function () {
                })
                .catch(function (error) {
                    return;
                });
            database.ref('User/').orderByChild('fname').equalTo(oldInstructor[0])
                .once('value', function(ins_user){
                    ins_user.forEach(function(keyNode){
                        console.log("KEY OF PREVIOUS INSTRUCTOR : " + keyNode.key);
                        if(keyNode.val().level.localeCompare("INSTRUCTOR") != 1){
                            database.ref('User/' + keyNode.key + '/courses/' + course_code).remove();
                        }
                    });
            });
        });
    });
}

function getCoursesForInstructor(instructorDetails, callback) {
    var available_courses = [];
    var ins_data = instructorDetails.split(',');
    var fname = ins_data[0];
    var lname = ins_data[1];
    var courses= [];
    var i=0;
    for(i = 3; i< ins_data.length; i++){
        courses.push(ins_data[i]);
    }
    // console.log("INS COURSES: " + courses);

    var courseRef = database.ref('Course/');
    courseRef.once('value', function(snapshot) {
        snapshot.forEach(function(item) {
            var code = item.val().code;
            if(!(courses.indexOf(code) >= 0)){
                var name = item.val().courseName;
                var days = item.val().days;
                var time = item.val().time;
                var instructor = item.val().instructorName;
                var listing = [code, name, days, time, instructor];
                // console.log("AVAILABLE COURSES : " + code);
                available_courses.push(listing);   
            }
        });
        return callback(available_courses);
    });
}

function changePrivilege(level){
    var arr = level.split(',');
    var fName = arr[0];
    var lName = arr[1];
    var lev = arr[2];
    if(lev.localeCompare("STUDENT") == 1){
        var userQry = database.ref('User/').orderByChild("fname").equalTo(fName.substring(2));
        //console.log(userQry)
        userQry.once('value', function (data) {
            if (!data.exists()) {
                //res.send('No users found')
            }
            else {
                data.forEach(function (user) {
                    database.ref('User/' + user.key).update({ level: "INSTRUCTOR" })
                        .then(function () {
                            return;
                        })
                        .catch(function (error) {
                            return;
                        });
                });
            }
        });
    }
    else{
        var userQry = database.ref('User/').orderByChild("fname").equalTo(fName.substring(2));
        //console.log(userQry)
        userQry.once('value', function (data) {
            if (!data.exists()) {
                //res.send('No users found')
            }
            else {
                data.forEach(function (user) {
                    database.ref('User/' + user.key).update({ level: "STUDENT" })
                        .then(function () {
                            return;
                        })
                        .catch(function (error) {
                            return;
                        });
                });
            }
        });
    }
    return;
}

function getUsers(callback) {
    var userRef = database.ref('User/');
    userRef.once('value', function(snapshot) {
        var userList = [];

        snapshot.forEach(function(item) {
            var firstName = item.val().fname;
            var lastName = item.val().lname;
            var level = item.val().level;
            var user = [firstName, lastName, level];
            userList.push(user);
        });
        //console.log(userList);
        return callback(userList);
    });
}

function getInstructors(callback) {
    var userRef = database.ref('User/');
    userRef.once('value', function(snapshot) {
        var instructorList = [];

        snapshot.forEach(function(item) {
            var firstName = item.val().fname;
            var lastName = item.val().lname;
            var level = item.val().level;
            var courses = JSON.stringify(item.val().courses);
            if(level.localeCompare("INSTRUCTOR") == 0){
                var regex = new RegExp(/true|"|:|{|}/gi);
                var str1 = courses.replace(regex, '');
                var instructor = [firstName, lastName, level, str1];
                instructorList.push(instructor);
            }
        });
        return callback(instructorList);
    });
}

function updateUserPrivilege(callback) {
    var userRef = database.ref('User/');
    userRef.once('value', function(snapshot) {
        var userList = [];

        snapshot.forEach(function(item) {
            var firstName = item.val().fname;
            var lastName = item.val().lname;
            var level = item.val().level;
            var user = [firstName, lastName, level];
            userList.push(user);
        });
        //console.log(userList);
        return callback(userList);
    });
}

router.post('/upload-csv', upload.single('document'), function (req, res) {
    const fileRows = [];

    // open uploaded file
    csv.parseFile(req.file.path)
        .on("data", function (data) {
            fileRows.push(data); // push each row
        })
        .on("end", function () {
            fs.unlinkSync(req.file.path);   // remove temp file
            //process "fileRows" and respond
            fileRows.forEach(loopthru);
            function loopthru(row, index)
            {
                database.ref('Course').push().set({
                    code: row[0],
                    courseName: row[1],
                    days: row[2],
                    time: row[3],
                    instructorName: row[4]
                });
            }

            res.status(200).send('Uploaded! <a href="/add_course">Go back</a>');
        })
});

firebase.auth().onAuthStateChanged(function(user) {
    if(user) {
        var loggedInUser = firebase.auth.currentUser;
        if(loggedInUser!=null) {

        }
    } else {
        
    }
});

router.use('/stats', checkAuth);

router.get('/stats', function(req, res) {
    getStats(function(labels, attendTrueList, attendLateList, attendFalseList, attendRatio) {
        res.render('stats', {
            labelList: labels,
            attendTrue: attendTrueList,
            attendLate: attendLateList,
            attendFalse: attendFalseList,
            attendPercent: attendRatio
        });
    });

});

function getStats(callback) {
    var attendRef = database.ref('InstructorAttendance/');
    attendRef.once('value', function(snapshot) {
        var labels = [];
        var attendTrueList = [];
        var attendLateList = [];
        var attendFalseList = [];
        var totalAttended = 0;
        var totalClasses = 0;
        var attendRatio = 0;

        snapshot.forEach(function(item) {
            var attendTrue = 0;
            var attendLate = 0;
            var attendFalse = 0;

            console.log(item.key);
            labels.push(item.key);
            for (date in item.val()) {
                console.log("Date ", date);
                console.log("child of date ", item.child(date));
                for (attendance in item.child(date).val()) {
                    console.log("Attendance ", attendance);
                    console.log("value of attendance ", item.child(date + "/" + attendance).val());
                    if (item.child(date + "/" + attendance).val().toLocaleString().localeCompare("true") == 0)
                        attendTrue++;
                    else if (item.child(date + "/" + attendance).val().toLocaleString().localeCompare("late") == 0)
                        attendLate++;
                    else
                        attendFalse++;
                }

            }

            attendTrueList.push(attendTrue);
            attendLateList.push(attendLate);
            attendFalseList.push(attendFalse);
            totalAttended += attendTrue + attendLate;
            totalClasses += attendTrue + attendLate + attendFalse;

            console.log("true list ", attendTrueList);
            console.log("late list ", attendLateList);
            console.log("false list ", attendFalseList);


            attendRatio = Math.round(totalAttended / totalClasses * 100);
        });

        return callback(labels, attendTrueList, attendLateList, attendFalseList, attendRatio);
    });
}

module.exports = router;
