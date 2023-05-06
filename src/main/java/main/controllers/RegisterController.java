package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.data.UserRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;


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
		return "register";
	}

	@PostMapping("/process-register")
	public String processRegister(@ModelAttribute("user") User user,
								  @RequestParam("repassword") String repassword
								  ,HttpSession session) {
		if(!user.getPassword().equals(repassword)) {
			session.setAttribute("user",user);
			session.setAttribute("repassword", repassword);
			return "redirect:register";
		}else {
			User user1 = userRepository.findByEmail(user.getEmail());
			if(user1!=null) {
				session.setAttribute("user",user);
				session.setAttribute("repassword", repassword);
				return "redirect:register";
			}else {
				if(user.getRole().equals("STUDENT")) {
					Student student = new Student();
					user.setStudent(student);
					userRepository.save(user);
					return "success_registration.html";
				}else{
					Teacher teacher = new Teacher();
					user.setTeacher(teacher);
					userRepository.save(user);
					return "success_registration.html";
				}
			}
		}

	}
}
