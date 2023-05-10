package main.api;

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
    private final QuestionRepository questionRepository;
    @Autowired
    QuestionApi(QuestionRepository questionRepository){
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
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(authentication.getName().equals(question.getExamination().getCourse().getTeacher().getUser().getUsername())){
            questionRepository.delete(question);
        }
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question, Authentication authentication){
        if(question.getDescription().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cần có đề bài");
        Question origin = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!origin.getId().equals(question.getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(authentication.getName().equals(question.getExamination().getCourse().getTeacher().getUser().getUsername())){
            origin.setAnswer(question.getAnswer());
            origin.setDescription(question.getDescription());
            origin.setCorrectAnswer(question.getCorrectAnswer());
            return questionRepository.save(origin);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
