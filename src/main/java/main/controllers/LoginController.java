package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.data.UserRepository;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

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
        User user = userRepository.findByEmail(email);
        if(user!=null){
            if(user.getPassword().equals(password)){
                if(user.getRole().equals("STUDENT")){
                    session.setAttribute("user", user);
                    return "redirect:home";
                }else{
                    session.setAttribute("user", user);
                    return "redirect:home";
                }
            }else {
                session.setAttribute("email", email);
                return "redirect:login";
            }
        }else{
            return "redirect:login";
        }
    }
}
