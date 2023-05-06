package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home1(){
        return "_home";
    }
    @GetMapping("/home")
    public String home(@RequestParam(value = "role",required = false) String role){
        if(role.equals("student")){
            return "homestudent";
        }
        if(role.equals("teacher")){
            return "hometeacher";
        }
        return "_home";
    }

}
