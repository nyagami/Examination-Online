package main.controllers;

import main.data.*;
import main.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/examination")
public class ExaminationController {
    private final ExaminationRepository examinationRepository;
    private final CourseRepository courseRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    @Autowired
    public ExaminationController(ExaminationRepository examinationRepository, ResultRepository resultRepository,
                                 TeacherRepository teacherRepository, StudentRepository studentRepository,
                                 UserRepository userRepository, CourseRepository courseRepository){
        this.examinationRepository = examinationRepository;
        this.courseRepository = courseRepository;
        this.resultRepository = resultRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String examination(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Examination examination = examinationRepository.findById(id).orElseThrow();
        Course course = examination.getCourse();
        if(user.getRole().equals("TEACHER")){
            Teacher teacher = teacherRepository.findByUser(user);
            if(course.getTeacher().equals(teacher)){
                model.addAttribute("examination", examination);
            }else throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else if(user.getRole().equals("STUDENT")){
            Student student = studentRepository.findByUser(user);
            if(course.getStudentList().contains(student)){
                model.addAttribute("examination", examination);
            }else throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return "examination";
    }
}
