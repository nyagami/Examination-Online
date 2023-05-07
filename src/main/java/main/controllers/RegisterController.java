package main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	private BCryptPasswordEncoder  passwordEncoder = new BCryptPasswordEncoder();

	@GetMapping("/register")
	public String register(@RequestParam(value = "error",required = false) String error,HttpSession session,Model model){
		if(error!=null) {
			if(error.equals("password"))
				model.addAttribute("error", "Xác nhận mật khẩu của bạn không đúng");
			else if(error.equals("email")) {
				model.addAttribute("error","Tài khoản của bạn đã tồn tại");
			}
		}
		User  user = (User) session.getAttribute("user");
		if(user==null) {
			user = new User();
		}
		String repassword = (String) session.getAttribute("repassword");
		if(repassword!=null) {
			model.addAttribute("repassword", repassword);
		}
		model.addAttribute("user",user);
		return "registration";
	}

	@PostMapping("/process-register")
	public String processRegister(@ModelAttribute("user") User user,
								  @RequestParam("repassword") String repassword
			,HttpSession session, Errors errors) {
		if(!user.getPassword().equals(repassword)) {
			session.setAttribute("user",user);
			session.setAttribute("repassword", repassword);
			return "redirect:register?error=password";
		}else {
			User user1 = userRepository.findByEmail(user.getEmail());
			if(user1!=null) {
				session.setAttribute("user",user);
				session.setAttribute("repassword", repassword);
				return "redirect:register?error=email";
			}else {
				if(user.getRole().equals("STUDENT")) {
					Student student = new Student();
					studentRepository.save(student);
					user.setStudent(student);
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					userRepository.save(user);
					return "success_registration.html";
				}else{
					Teacher teacher = new Teacher();
					teacherRepository.save(teacher);
					user.setTeacher(teacher);
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					userRepository.save(user);
					return "success_registration.html";
				}
			}
		}

	}
}
