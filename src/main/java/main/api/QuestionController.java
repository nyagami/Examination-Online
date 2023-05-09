package main.api;

import main.data.ExaminationRepository;
import main.data.QuestionRepository;
import main.models.Examination;
import main.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/question", produces = "application/json")
@CrossOrigin("*")
public class QuestionController {
    private final ExaminationRepository examinationRepo;
    private final QuestionRepository questionRepo;
    @Autowired
    QuestionController(ExaminationRepository examinationRepo, QuestionRepository questionRepo){
        this.examinationRepo = examinationRepo;
        this.questionRepo = questionRepo;
    }

    @PostMapping("/add/{examinationId}")
    public Question addQuestion(@PathVariable("examinationId") Long examinationId, @RequestBody Question question){
        Examination examination = examinationRepo.findById(examinationId).orElseThrow();
        examinationRepo.save(examination);
        return questionRepo.save(question);
    }

    @DeleteMapping("/delete/{examinationId}")
    public void deleteQuestion(@PathVariable("examinationId") Long examinationId, @RequestBody Question question){
        Examination examination = examinationRepo.findById(examinationId).orElseThrow();
        examinationRepo.save(examination);
    }

    @PutMapping("/update")
    public Question updateQuestion(@PathVariable("id") Long id, @RequestBody Question question){
        return questionRepo.save(question);
    }
}
