function capnhap(){
	var name = document.getElementById("name").value;
	var email = document.getElementById("email").value;
	var phoneNumber = document.getElementById("phoneNumber").value;
	var date = document.getElementById("dateOfBirth").value;
	var address = document.getElementById("address").value;
	var a = date.split("/");
	var date1 = a[2]+"-"+a[1]+"-"+a[0];
	var dateOfBirth = new Date(date1);
	if(!isNaN(dateOfBirth.getTime())){
		user = {
	    name, email, phoneNumber, dateOfBirth, address,
		}
		fetch("/api/profile", {
		    method: 'PUT',
		    headers: {
		        'Accept': 'application/json',
		        'Content-Type': 'application/json'
		    },
		    body: JSON.stringify(user),
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
	else{
		alert("Bản phải điền đúng ngày tháng")
	}
}
