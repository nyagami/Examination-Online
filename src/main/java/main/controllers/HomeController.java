package main.controllers;

import main.data.CourseRepository;
import main.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;

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
        return "hometeacher";
    }
}
