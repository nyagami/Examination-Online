package main.api;

import jakarta.validation.Valid;
import main.data.*;
import main.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/examination", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
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
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userRepository.findByUsername(authentication.getName());
        if(user.getRole().equals("TEACHER")){
            if(course.getTeacher().getUser().equals(user)){
                return examinationRepository.findByCourse(course);
            }
        }else if(user.getRole().equals("STUDENT")){
            List<Student> studentList = course.getStudentList();
            Student student = studentRepository.findByUser(user);
            if(studentList.contains(student)) return examinationRepository.findByCourseAndIsVisible(course, true);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/add/{courseId}")
    public Examination create(@PathVariable("courseId") Long courseId, @Valid @RequestBody Examination examination, Authentication authentication){
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Teacher teacher = course.getTeacher();
        if(!teacher.getUser().getUsername().equals(authentication.getName())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        examination.setCourse(course);
        return examinationRepository.save(examination);
    }

    @PutMapping("/update")
    public Examination update(@Valid @RequestBody Examination examination, Authentication authentication){
        Examination origin = examinationRepository.findById(examination.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        examination.setCourse(origin.getCourse());
        return examinationRepository.save(examination);
    }
    @DeleteMapping("/delete/{id}")
    public void delete (@PathVariable("id") Long id, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Teacher teacher = teacherRepository.findByUser(user);
        if(teacher == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Examination examination = examinationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        examinationRepository.delete(examination);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> apiError(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
