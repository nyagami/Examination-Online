package main.api;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import main.data.CourseRepository;
import main.data.StudentRepository;
import main.data.TeacherRepository;
import main.data.UserRepository;
import main.models.Course;
import main.models.Student;
import main.models.Teacher;
import main.models.User;


@RequestMapping(path = "/api/course", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RestController
public class CourseAPI {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseAPI(CourseRepository courseRepository, TeacherRepository teacherRepository, UserRepository userRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.studentRepository=studentRepository;
    }

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public Course addCourse(@RequestBody Course course, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Teacher teacher = teacherRepository.findByUser(user);
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    @PostMapping("/joinCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public Course joinCourse(@RequestBody Course course, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Student student = studentRepository.findByUser(user);
        if(student==null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Course course_1 = courseRepository.findByCode(course.getCode());
        if(course_1==null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<Student> students = course_1.getStudentList();
        students.add(student);
        course_1.setStudentList(students);
        return courseRepository.save(course_1);
    }
    @PutMapping("/leaveCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public Course leaveCourse(@RequestBody Course course, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Student student = studentRepository.findByUser(user);
        if(student==null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Course course_1 = courseRepository.findAllById(course.getId());
        if(course_1==null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<Student> students = course_1.getStudentList();
        if(students.contains(student)) {
        	students.remove(student);
        }
        course_1.setStudentList(students);
        return courseRepository.save(course_1);
    }
    @DeleteMapping("delete/{id}")
    public void deleteCourse(@PathVariable("id") Long courseId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Teacher teacher = teacherRepository.findByUser(user);
        if(teacher == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Optional<Course> course = courseRepository.findById(courseId);
        courseRepository.delete(course.get());
    }
}
