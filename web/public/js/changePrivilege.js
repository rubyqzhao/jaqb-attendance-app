// import require from 'require'
// require('./index.js');
function changePrivilege(level){
    $.ajax({
            type: "POST",
            url: "/change_privilege",
            timeout: 5000,
            data: level,
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