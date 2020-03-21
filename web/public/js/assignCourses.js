//get all courses
function addCourses(instructor){
    console.log(instructor);
    $.ajax({
        url: '/all_courses',
        type: 'GET',
        data: {"ins_data": instructor},
        success: function(res) {
            document.write(res);
        },
        error: function(jqXHR, textStatus, err) {
            //show error message
            alert('text status '+textStatus+', err '+err)
        }
    });
}

function addCoursesToInstructor(course_details, instructor){
    console.log("COURSE DETAILS: " + course_details);
    console.log("INSTRUCTOR : " + instructor);
    // var r = confirm("Do !");
    // if (r == true) {
    //     txt = "You pressed OK!";
    // } 
    // else {
    //     txt = "You pressed Cancel!";
    // }
    $.ajax({
        type: "POST",
        url: "/",
        timeout: 5000,
        data: {
            "course_details": course_details,
            "instructor": instructor
        },
        success: function(data) {
            //show content
            alert('Success!');
            window.location.reload();
        },
        error: function(jqXHR, textStatus, err) {
            //show error message
            alert('text status '+textStatus+', err '+err)
        }
    });
}