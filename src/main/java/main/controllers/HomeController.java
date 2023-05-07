package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    
    @GetMapping("/403")
    public String page403() {
    	return "404";
    }
    
    @GetMapping("/student/home")
    public String home1(){
        return "homestudent";
    }
    @GetMapping("/teacher/home")
    public String home2(){
        return "hometeacher";
    }
}
