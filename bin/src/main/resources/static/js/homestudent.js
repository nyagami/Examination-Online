function joinCourse(){
    var code = document.getElementById("code").value;
    course = {
        code,
    }
    fetch("/api/course/joinCourse", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(course),
    }).then(response  => {
        if(response.ok) {
            alert('Vào lớp thành công');
            window.location.href=window.location.href
        } else{
            alert('Bạn không thể vào lớp');
        }
    })
        .catch(error => {
            alert(error.message);
        });
}
function leaveCourse(ele){
    var id = ele.getAttribute("data-id");
    course = {
        id,
    }
    fetch("/api/course/leaveCourse", {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(course),
    }).then(response  => {
        if(response.ok) {
            alert('Rời lớp thành công');
            window.location.href=window.location.href
        } else{
            alert('Bạn không thể rời lớp');
        }
    })
        .catch(error => {
            alert(error.message);
        });
}