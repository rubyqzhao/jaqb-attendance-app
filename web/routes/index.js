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
router.get('/', function(req, res, next) {
    getCourses(function(courseList) {
        res.render('index', {
            title: 'JAQB Admin',
            courses: courseList
        });
    });
});

function getCourses(callback) {
    var courseRef = database.ref('Course/');
    courseRef.on('value', function(snapshot) {
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
        console.log(courseList);
        return callback(courseList);
    });
}


module.exports = router;
