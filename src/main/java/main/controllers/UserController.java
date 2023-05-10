package main.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/profile")
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
			model.addAttribute("user",teacher);
		}else {
			Student student = studentRepository.findByUser(user);
			if(student.getDateOfBirth()!=null) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String date = format.format(student.getDateOfBirth());
				model.addAttribute("dateOfBirth",date);
			}
			model.addAttribute("user",student);
		}
		return "profile";
	}
}
