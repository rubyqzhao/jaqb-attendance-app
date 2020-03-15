var express = require('express');
var router = express.Router();
var firebase = require("firebase/app");
require("firebase/database");
require('dotenv').config();

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

router.post('/add-course', function (req, res) {
    var response = req.body;
    console.log(response);
    //if input isn't entered or invalid, prevent send
    if(response == null || response.code == null || response.name == null
        || response.days == null || response.instructor == null || response.time == null) {
        res.send('Submission is invalid');
    }
    else if(response.code.localeCompare("") == 0 || response.name.localeCompare("") == 0
        || response.days.localeCompare("") == 0 || response.instructor.localeCompare("") == 0
        || response.time.localeCompare("") == 0) {
        res.send('Missing information');
    }
    else {
        database.ref('Course/').push().set({
            code: response.code,
            courseName: response.name,
            days: response.days,
            instructorName: response.instructor,
            time: response.time
        });
        res.send('Submitted');
    }
});

router.post('/delete-course', function(req, res) {
    var response = req.body;
    if (response == null || response.courseCode == null)
        res.send('Invalid POST');
    else if(response.courseCode.localeCompare("") == 0) {
        res.send('Please enter a course code');
    }
    else {
        var userQuery = database.ref('Course/').orderByChild("code").equalTo(response.courseCode);
        console.log(userQuery)
        userQuery.once('value', function (data) {
            if (!data.exists()) {
                res.send('No users found')
            }
            else {
                data.forEach(function (user) {
                    database.ref('Course/' + user.key).remove()
                        .then(function () {
                            res.send("Remove succeeded.")
                        })
                        .catch(function (error) {
                            res.send("Remove failed: " + error.message)
                        });
                });
            }
        });
    }
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

router.get('/user_privileges_page', function(req, res) {
    getUsers(function(userList) {
        res.render('user_privileges', {
            title: 'User Privileges',
            users: userList
        });
    });
});

router.post('/change_privilege', function(req, res) {
    changePrivilege(JSON.stringify(req.body));
    res.redirect('/');
});


router.get('/user_privileges_page', function(req, res) {
  res.render('user_privileges', { title: 'Add classes to Instructors' });
});

function changePrivilege(level){
    var arr = level.split(',');
    var fName = arr[0];
    var lName = arr[1];
    var lev = arr[2];
    if(lev.localeCompare("STUDENT") == 1){
        var userQry = database.ref('User/').orderByChild("fname").equalTo(fName.substring(2));
        console.log(userQry)
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
        console.log(userQry)
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
        console.log(userList);
        return callback(userList);
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
        console.log(userList);
        return callback(userList);
    });
}

module.exports = router;
