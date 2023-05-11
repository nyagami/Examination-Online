var modal = document.getElementById("modal");
var btn = document.getElementById("btnModal");
var btn1 = document.getElementById("trolai");
var manh = document.getElementById("name");
btn1.onclick = function() {
    modal.style.display = "none";
    manh.value=""

}
btn.onclick = function() {
    modal.style.display = "flex";
}
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
function addCourse(){
    var name = document.getElementById("name").value;
    course = {
        name,
    }
    fetch("/api/course/addCourse", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(course),
    }).then(response  => {
        if(response.ok) {
            alert('Thêm lớp thành công');
            window.location.href=window.location.href
        } else{
            alert('Thêm thất bại');
        }
    })
        .catch(error => {
            alert(error.message);
        });
}
function deleteCourse(ele){
    var courseId = ele.getAttribute("data-id");
    var api = '/api/course/delete/'+courseId;
    fetch(api ,{
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    }).then(response  => {
        if(response.ok) {
            alert('Xóa lớp thành công');
            window.location.href=window.location.href
        } else{
            alert('Xóa thất bại');
        }
    })
        .catch(error => {
            alert(error.message);
        });
}
