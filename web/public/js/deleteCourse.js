function deleteCourse(course){
    $.ajax({
        type: "POST",
        url: "/delete-course",
        timeout: 5000,
        data: course,
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