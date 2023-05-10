package main.api;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;

@RequestMapping(path = "/api/profile", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RestController
public class UserApi {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	@PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
	public Teacher updateProfile(@RequestBody Teacher teacher, Principal principal) {
		User user = userRepository.findByUsername(principal.getName());
		if(user==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		if (user.getRole().equals("TEACHER")) {
			Teacher teacher_1 = teacherRepository.findByUser(user);
			teacher_1.setName(teacher.getName());
			teacher_1.setAddress(teacher.getAddress());
			teacher_1.setDateOfBirth(teacher.getDateOfBirth());
			teacher_1.setEmail(teacher.getEmail());
			teacher_1.setPhoneNumber(teacher.getPhoneNumber());
			return teacherRepository.save(teacher_1);
		}else {
			Student student = studentRepository.findByUser(user);
			student.setName(teacher.getName());
			student.setAddress(teacher.getAddress());
			student.setDateOfBirth(teacher.getDateOfBirth());
			student.setEmail(teacher.getEmail());
			student.setPhoneNumber(teacher.getPhoneNumber());
			studentRepository.save(student); 
			return teacher;
		}
		
	}
}
