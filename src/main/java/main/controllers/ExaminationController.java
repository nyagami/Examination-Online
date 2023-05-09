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

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/examination")
public class ExaminationController {
    private final ExaminationRepository examinationRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    @Autowired
    public ExaminationController(ExaminationRepository examinationRepository, TeacherRepository teacherRepository,
                                 StudentRepository studentRepository, UserRepository userRepository,
                                 QuestionRepository questionRepository, ResultRepository resultRepository){
        this.examinationRepository = examinationRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/{id}")
    public String examination(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Examination examination = examinationRepository.findById(id).orElseThrow();
        Course course = examination.getCourse();
        if(user.getRole().equals("TEACHER")){
            Teacher teacher = teacherRepository.findByUser(user);
            if(!course.getTeacher().equals(teacher))
                throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else if(user.getRole().equals("STUDENT") && examination.getIsVisible()){
            Student student = studentRepository.findByUser(user);
            if(!course.getStudentList().contains(student) || !examination.getIsVisible())
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            Result result = resultRepository.findByExaminationAndStudent(examination, student);
            if(result != null){
                Date now = new Date();
                Long remain = examination.getTotalTime() - (now.getTime() - result.getStartTime().getTime())/1000;
                model.addAttribute("remain", remain);
            }else{
                model.addAttribute("remain", examination.getTotalTime());
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("exam", examination);
        model.addAttribute("role", user.getRole());
        model.addAttribute("startDate", simpleDateFormat.format(examination.getStartDate()));
        model.addAttribute("endDate", simpleDateFormat.format(examination.getEndDate()));
        Iterable<Question> questions = questionRepository.findByExamination(examination);
        model.addAttribute("questions", questions);
        return "examination";
    }

    @GetMapping("/{id}/process")
    public String process(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        if(user == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Examination examination = examinationRepository.findById(id).orElseThrow();
        Student student = studentRepository.findByUser(user);
        if(!examination.getCourse().getStudentList().contains(student))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        Result result = resultRepository.findByExaminationAndStudent(examination, student);
        if(result == null){
            result = new Result();
            result.setExamination(examination);
            result.setStudent(student);
            result.setStartTime(new Date());
            result.setNumberOfCorrectAnswers(0);
            resultRepository.save(result);
        }
        Date now = new Date();
        long remain = examination.getTotalTime() - (now.getTime() - result.getStartTime().getTime())/1000;
        if(remain < 0){
            model.addAttribute("done", true);
        }else{
            model.addAttribute("done", false);
            Iterable<Question> questions = questionRepository.findByExamination(examination);
            model.addAttribute("questions", questions);
            model.addAttribute("remain", remain);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("startDate", simpleDateFormat.format(examination.getStartDate()));
        model.addAttribute("endDate", simpleDateFormat.format(examination.getEndDate()));
        model.addAttribute("exam", examination);
        return "process_examination";
    }
}
