var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'JAQB Admin' });
});

router.get('/instructorToClasses', function(req, res, next) {
  res.render('assign_classes', { title: 'Add classes to Instructors' });
});

module.exports = router;
