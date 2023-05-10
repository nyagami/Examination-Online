package main.controllers;

import main.data.CourseRepository;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Course;
import main.models.Student;
import main.models.Teacher;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String root(Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        if(user.getRole().equals("STUDENT"))
            return "redirect:/student/home";
        else if(user.getRole().equals("TEACHER")){
            return "redirect:/teacher/home";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/home")
    public String home(){
        return "redirect:/";
    }

    @GetMapping("/student/home")
    public String home1(Principal principal,Model model){
        User user = userRepository.findByUsername(principal.getName());
        Student student = studentRepository.findByUser(user);
        List<Course> courses = courseRepository.findAll();
        if(courses!=null) {
            List<Course> coursess = new ArrayList<>();
            for(Course course:courses) {
                if(course.getStudentList().contains(student)) {
                    coursess.add(course);
                }
            }
            if(coursess.size()!=0)
                model.addAttribute("courses",coursess);
        }
        return "homestudent";
    }
    @GetMapping("/teacher/home")
    public String home2(Principal principal,Model model){
        User user = userRepository.findByUsername(principal.getName());
        Teacher teacher = teacherRepository.findByUser(user);
        List<Course> courses = courseRepository.findByTeacher(teacher);
        if(courses!=null) {
            model.addAttribute("courses",courses);
        }
        return "hometeacher";
    }
}
