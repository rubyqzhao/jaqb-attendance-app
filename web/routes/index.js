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
    res.send('Deleted');
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

router.get('/user_privileges_page', function(req, res, next) {
    getUsers(function(userList) {
        res.render('user_privileges', {
            title: 'User Privileges',
            users: userList
        });
    });
});

router.get('/user_privileges_page', function(req, res, next) {
  res.render('user_privileges', { title: 'Add classes to Instructors' });
});

function getUsers(callback) {
    var userRef = database.ref('User/');
    userRef.on('value', function(snapshot) {
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
    userRef.on('value', function(snapshot) {
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
