package main.controllers;

import lombok.Data;
import main.data.*;
import main.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/examination")
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
    public void detailDate(Examination examination, Model model){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("startDate", simpleDateFormat.format(examination.getStartDate()));
        model.addAttribute("endDate", simpleDateFormat.format(examination.getEndDate()));
    }

    @GetMapping("/{id}")
    public String examination(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Examination examination = examinationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Course course = examination.getCourse();
        if(user.getRole().equals("TEACHER")){
            Teacher teacher = teacherRepository.findByUser(user);
            if(!course.getTeacher().equals(teacher))
                throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
            Iterable<Question> questions = questionRepository.findByExamination(examination);
            model.addAttribute("questions", questions);
        }else if(user.getRole().equals("STUDENT") && examination.getIsVisible()){
            Student student = studentRepository.findByUser(user);
            if(!course.getStudentList().contains(student))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            Result result = resultRepository.findByExaminationAndStudent(examination, student);
            Date now = new Date();
            if(result != null){
                Long remain = result.getDone() ? 0 : examination.getTotalTime() - (now.getTime() - result.getStartTime().getTime())/1000;
                model.addAttribute("remain", remain);
            }else{
                model.addAttribute("remain", examination.getTotalTime());
            }
            String status = (now.after(examination.getStartDate()) && now.before(examination.getEndDate())) ? "DURING" :
                    (now.after(examination.getEndDate()) ? "ENDED" : "COMING");
            model.addAttribute("status", status);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        model.addAttribute("exam", examination);
        model.addAttribute("role", user.getRole());
        detailDate(examination, model);
        return "examination";
    }

    @GetMapping("/{id}/process")
    public String process(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Examination examination = examinationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Student student = studentRepository.findByUser(user);
        if(!examination.getCourse().getStudentList().contains(student) || !examination.getIsVisible())
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
            model.addAttribute("done", result.getDone());
            Iterable<Question> questions = questionRepository.findByExamination(examination);
            model.addAttribute("questions", questions);
            for(Question q: questions){
                q.setCorrectAnswer(0);
            }
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setQuestions((List<Question>) questions);
            model.addAttribute("questionWrapper", questionWrapper);
            model.addAttribute("remain", remain);
        }
        detailDate(examination, model);
        model.addAttribute("exam", examination);
        return "process_examination";
    }

    @Data
    public static class QuestionWrapper{
        private List<Question> questions;
    }

    @PostMapping(value = "/{id}/process")
    public String processSubmit(@PathVariable("id") Long id, @ModelAttribute QuestionWrapper questionWrapper, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Student student = studentRepository.findByUser(user);
        Examination examination = examinationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Result result = resultRepository.findByExaminationAndStudent(examination, student);
        if(result == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        result.setDone(true);
        List<Question> questions = (List<Question>) questionRepository.findByExamination(examination);
        List<Question> studentQuestions = questionWrapper.getQuestions();
        int numberOfCorrectAnswers = 0;
        for(int i = 0; i < questions.size(); i ++ ){
            if(questions.get(i).getCorrectAnswer().equals(studentQuestions.get(i).getCorrectAnswer())){
                numberOfCorrectAnswers += 1;
            }
        }
        result.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        resultRepository.save(result);
        return "redirect:/examination/"+id+"/results";
    }

    @GetMapping("{id}/results")
    public String results(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Examination examination = examinationRepository.findById(id).orElseThrow();
        Course course = examination.getCourse();
        if(user.getRole().equals("TEACHER")){
            Teacher teacher = teacherRepository.findByUser(user);
            if(!course.getTeacher().equals(teacher))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else if(user.getRole().equals("STUDENT") && examination.getIsVisible()){
            Student student = studentRepository.findByUser(user);
            if(!course.getStudentList().contains(student))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        List<Question> questions = (List<Question>) questionRepository.findByExamination(examination);
        model.addAttribute("numberOfQuestions", questions.size());
        Iterable<Result> results = resultRepository.findByExaminationAndDone(examination, true);
        model.addAttribute("results", results);
        model.addAttribute("exam", examination);
        detailDate(examination, model);
        return "results";
    }
}
