package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@GetMapping("/register")
	public String register(@RequestParam(value = "error",required = false) String error, Model model, Authentication authentication){
		if(authentication!=null){
			User user = userRepository.findByUsername(authentication.getName());
			if(user.getRole().equals("STUDENT"))
				return "redirect:/student/home";
			else{
				return "redirect:/teacher/home";
			}
		}
		if(error!=null) {
			if(error.equals("password"))
				model.addAttribute("error", "Xác nhận mật khẩu của bạn không đúng");
			else if(error.equals("username")) {
				model.addAttribute("error","Tài khoản của bạn đã tồn tại");
			}
		}
		model.addAttribute("user", new User());
		return "registration";
	}
	@PostMapping("/process-register")
	public String processRegister(@ModelAttribute("user") User user, @RequestParam("password_2") String password_2,
								  @RequestParam("name") String name, HttpSession session, Errors errors) {
		if(!user.getPassword().equals(password_2)) {
			return "redirect:register?error=password";
		}else {
			User user1 = userRepository.findByUsername(user.getUsername());
			if(user1!=null) {
				return "redirect:register?error=username";
			}else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userRepository.save(user);
				if (user.getRole().equals("STUDENT")) {
					Student student = new Student();
					student.setName(name);
					student.setUser(user);
					studentRepository.save(student);
				} else {
					Teacher teacher = new Teacher();
					teacher.setName(name);
					teacher.setUser(user);
					teacherRepository.save(teacher);
				}
				return "success_registration.html";
			}
		}

	}
}
