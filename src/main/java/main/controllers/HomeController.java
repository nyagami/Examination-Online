package main.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import main.data.CourseRepository;
import main.data.UserRepository;
import main.models.Course;
import main.models.User;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model){
        List<Course> courses = courseRepository.findAll(Sort.by("id"));
        if(courses!=null) {
            model.addAttribute("courses",courses);
        }
        return "home";
    }

    @GetMapping("/403")
    public String page403() {
        return "403";
    }

    @GetMapping("/student/home")
    public String home1(){
        return "homestudent";
    }
    @GetMapping("/teacher/home")
    public String home2(Principal principal,Model model){
    	User user = userRepository.findByEmail(principal.getName());
    	List<Course> courses = courseRepository.findByTeacher(user.getTeacher());
    	if(courses!=null) {
    		model.addAttribute("courses",courses);
    	}
        return "hometeacher";
    }
}
