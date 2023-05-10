package main.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/profile")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping
	public String profile(Principal principal,Model model) {
		User user = userRepository.findByUsername(principal.getName());
		if(user==null) {
			return "redirect:/login";
		}
		if(user.getRole().equals("TEACHER")) {
			Teacher teacher =  teacherRepository.findByUser(user);
			if(teacher.getDateOfBirth()!=null) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String date = format.format(teacher.getDateOfBirth());
				model.addAttribute("dateOfBirht",date);
			}
			model.addAttribute("profile", teacher);
		}else {
			Student student = studentRepository.findByUser(user);
			if(student.getDateOfBirth()!=null) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String date = format.format(student.getDateOfBirth());
				model.addAttribute("dateOfBirth",date);
			}
			model.addAttribute("profile", student);
		}
		return "profile";
	}
	@Data
	public static class Profile{
		private String email;
		@NotBlank(message = "Cần phải có tên")
		private String name;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date dateOfBirth;
		private String address;
		private String phoneNumber;

		public Teacher toTeacher(Teacher teacher){
			teacher.setName(this.name);
			teacher.setEmail(this.email);
			teacher.setDateOfBirth(this.dateOfBirth);
			teacher.setPhoneNumber(this.phoneNumber);
			teacher.setAddress(this.address);
			return teacher;
		}
		public Student toStudent(Student student){
			student.setName(this.name);
			student.setEmail(this.email);
			student.setDateOfBirth(this.dateOfBirth);
			student.setPhoneNumber(this.phoneNumber);
			student.setAddress(this.address);
			return student;
		}
	}

	@PostMapping
	public String update(@Valid Profile profile, Errors errors, Authentication authentication, Model model){
		if(errors.hasErrors()){
			return "profile";
		}
		User user = userRepository.findByUsername(authentication.getName());
		if(user.getRole().equals("TEACHER")){
			Teacher teacher = teacherRepository.findByUser(user);
			model.addAttribute("profile", teacherRepository.save(profile.toTeacher(teacher)));
		}else{
			Student student = studentRepository.findByUser(user);
			model.addAttribute("profile", studentRepository.save(profile.toStudent(student)));
		}
		return "profile";
	}
}
