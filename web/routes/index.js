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


const firebaseConfig = {
    apiKey: process.env.API_KEY,
    authDomain: process.env.AUTH_DOMAIN,
    databaseURL: "https://jaqb-attendance-app.firebaseio.com",
    projectId: "jaqb-attendance-app",
    storageBucket: "jaqb-attendance-app.appspot.com",
    messagingSenderId: process.env.MSG_ID,
    appId: process.env.APP_ID,
    measurementId: process.env.MEASURE_ID
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
var database = firebase.database();

/* GET home page. */
router.get('/', function(req, res) {
    getCourses(function(courseList) {
        res.render('index', {
            title: 'JAQB Admin',
            courses: courseList
        });
    });
});

// opens the page with list of users, enables admin to change user privileges
router.get('/user_privileges_page', function(req, res) {
    getUsers(function(userList) {
        res.render('user_privileges', {
            title: 'User Privileges',
            users: userList
        });
    });
});

// opens list of instructors in the app, enables admin to assign courses to them
router.get('/assign_courses', function(req, res) {
    getInstructors(function(instructorList) {
        res.render('all_instructors', {
            title: 'instructors',
            instructors: instructorList
        });
    });
});

// get courses in page - add courses to the instructor
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
    res.redirect('/');
});

router.post('/add_course_to_instructor', function(req, res) {
    //console.log(JSON.stringify(req.body) + " : BODY");
    addCourseToInstructor(JSON.stringify(req.body));
    res.redirect('/');
});

router.post('/add-course', function (req, res) {
    var response = req.body;
    console.log(req.body);
    console.log(response);
    //if input isn't entered or invalid, prevent send
    if(response == null || response.code == null || response.name == null
        || response.days == null || response.instructor == null || response.time == null) {
        res.send('Submission is invalid');
    }
    else if(response.code.localeCompare("") == 0 || response.name.localeCompare("") == 0
        || response.days == [""] || response.instructor.localeCompare("") == 0
        || response.time.localeCompare("") == 0) {
        res.send('Missing information');
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
        res.send('Submitted');
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

    res.redirect('/');
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

            res.send('Uploaded');
        })
});

module.exports = router;
