package main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.models.Student;
import main.models.Teacher;

@Controller
public class LoginController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error",required = false) String error, Model model){
    	if(error!=null) {
	        if(error.equals("password")){
	            model.addAttribute("error","Mật khẩu của bạn không đúng");
	        }else if(error.equals("email")){
	            model.addAttribute("error","Tài khoản không tồn tại");
	        }
    	}
        return "login";
    }
    @PostMapping("/login-process")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password, HttpSession session,Model model){
        Student student = studentRepository.findByEmail(email);
        Teacher teacher = teacherRepository.findByEmail(email);
        if(student!=null){
            if(student.getPassword().equals(password)){
                session.setAttribute("student", student);
                return "redirect:/home?role=student";
            }else{
                session.setAttribute("email",email);
                return "redirect:/login?error=password";
            }
        }else if(teacher!=null){
            if(teacher.getPassword().equals(password)){
                session.setAttribute("teacher", teacher);
                return "redirect:/home?role=teacher";
            }else{
                session.setAttribute("email",email);
                return "redirect:/login?error=password";
            }
        }else{
            return "redirect:/login?error=email";
        }
    }
}
