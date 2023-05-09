package main.api;

import main.data.*;
import main.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/examination", produces = "application/json")
@CrossOrigin("*")
public class ExaminationApi {
    private final ExaminationRepository examinationRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    @Autowired
    public ExaminationApi(ExaminationRepository examinationRepository, CourseRepository courseRepository,
                          TeacherRepository teacherRepository, StudentRepository studentRepository,
                          UserRepository userRepository){
        this.examinationRepository = examinationRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/course/{courseId}")
    Iterable<Examination> findByCourse(@PathVariable("courseId") Long courseId, Authentication authentication){
        Course course = courseRepository.findAllById(courseId);
        if(course == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        User user = userRepository.findByUsername(authentication.getName());
        if(user.getRole().equals("TEACHER")){
            if(course.getTeacher().getUser().equals(user)){
                return examinationRepository.findByCourse(course);
            }
        }else if(user.getRole().equals("STUDENT")){
            List<Student> studentList = course.getStudentList();
            Student student = studentRepository.findByUser(user);
            for(Student std: studentList){
                if(std.equals(student)){
                    return examinationRepository.findByCourseAndIsVisible(course, true);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/add")
    public Examination create(@RequestBody Examination examination, Authentication authentication){
        Course course = examination.getCourse();
        if(course == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Teacher teacher = course.getTeacher();
        if(teacher == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return examinationRepository.save(examination);
    }


    @DeleteMapping("delete/{id}")
    public void delete (@PathVariable("id") Long id, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Teacher teacher = teacherRepository.findByUser(user);
        if(teacher == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Examination examination = examinationRepository.findById(id).orElseThrow();
        examinationRepository.delete(examination);
        return;
    }
}
