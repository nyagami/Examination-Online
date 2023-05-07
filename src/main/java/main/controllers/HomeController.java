package main.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import main.data.CourseRepository;
import main.models.Course;
import main.models.Student;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/home")
    public String home(Model model){
        List<Course> courses = courseRepository.findAll(Sort.by("id"));
        List<String> quantities = new ArrayList<>();
//    	for(Course course: courses) {
//    		String qtt = course.getStudents().size()+"/"+course.getQuantity();
//            quantity
//    	}
        if(courses!=null) {
            model.addAttribute("courses",courses);
        }
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
