//get all courses
function addCourses(instructor){
    console.log(instructor);
    $.ajax({
        url: '/all_courses',
        type: 'GET',
        data: {"ins_data": instructor},
        success: function(res) {
            console.log(res);
            document.write(res);
        },
        error: function(jqXHR, textStatus, err) {
            //show error message
            alert('text status '+textStatus+', err '+err)
        }
    });
}