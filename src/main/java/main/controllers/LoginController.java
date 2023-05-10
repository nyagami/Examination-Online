package main.controllers;

import main.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@Autowired
	private UserRepository userRepository;
	@GetMapping("/login")
	public String login(@RequestParam(value = "error",required = false) String error, @RequestParam(value="logout", required = false) String logout, Model model, Authentication authentication){
		if(authentication!=null){
			return "redirect:/";
		}
		if(error!=null) {
			if(error.equals("true")){
				model.addAttribute("error","Tài khoản hoặc mật khẩu của bạn không đúng");
			}
		}
		if(logout!=null) {
			if(logout.equals("success")) {
				model.addAttribute("logout","Đăng xuất thành công");
			}
		}
		return "login";
	}

}
