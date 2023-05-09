package main.api;

import main.data.ExaminationRepository;
import main.data.QuestionRepository;
import main.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/question", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class QuestionApi {
    private final ExaminationRepository examinationRepository;
    private final QuestionRepository questionRepository;
    @Autowired
    QuestionApi(ExaminationRepository examinationRepository, QuestionRepository questionRepository){
        this.examinationRepository = examinationRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping("/add")
    public Question add(@RequestBody Question question, Authentication authentication){
        if(question.getDescription().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cần có đề bài");
        if(authentication.getName().equals(question.getExamination().getCourse().getTeacher().getUser().getUsername())){
            return questionRepository.save(question);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable("id") Long id, Authentication authentication){
        Question question = questionRepository.findById(id).orElseThrow();
        if(authentication.getName().equals(question.getExamination().getCourse().getTeacher().getUser().getUsername())){
            questionRepository.delete(question);
        }
    }

    @PutMapping("/update")
    public Question updateQuestion(@PathVariable("id") Long id, @RequestBody Question question){
        return questionRepository.save(question);
    }
}
