package main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.models.Student;
import main.models.Teacher;
import main.models.User;

@Controller
public class RegisterController {
	
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	
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
		   @RequestParam("repassword") String repassword, 
		   @RequestParam("role") String role,HttpSession session) {
	   if(!user.getPassword().equals(repassword)) {
		   session.setAttribute("user",user);
		   session.setAttribute("repassword", repassword);
		   return "redirect:/register?error=password";
	   }else {
		   Teacher teacher = teacherRepository.findByEmail(user.getEmail());
		   Student student = studentRepository.findByEmail(user.getEmail());
		   if(teacher!=null|| student!=null) {
			   session.setAttribute("user",user);
			   session.setAttribute("repassword", repassword);
			   return "redirect:/register?error=email";
		   }else {
			   if(role.equals("student")) {
				   Student student1 = new Student();
				   student1.setEmail(user.getEmail());
				   student1.setPassword(user.getPassword());
				   student1.setName(user.getName());
				   studentRepository.save(student1);
				   return "success_registration.html";
			   }else {
				   Teacher teacher1 = new Teacher();
				   teacher1.setEmail(user.getEmail());
				   teacher1.setEmail(user.getEmail());
				   teacher1.setPassword(user.getPassword());
				   teacher1.setName(user.getName());
				   teacherRepository.save(teacher1);
				   return "success_registration.html";
			   }
		   }
	   }
	   
   }
}
