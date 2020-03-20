//get all courses
function getCourses(){
    $.ajax({
            type: "GET",
            url: "/all-courses",
            timeout: 5000,
            success: function(data) {
                //show content
                console.log(data);
                alert('Success!');
                window.location.reload();
            },
            error: function(jqXHR, textStatus, err) {
                //show error message
                alert('text status '+textStatus+', err '+err)
            }
        });
}