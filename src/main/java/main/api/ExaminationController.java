package main.api;

import main.data.ExaminationRepository;
import main.models.Examination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/examination", produces = "application/json")
@CrossOrigin("*")
public class ExaminationController {
    private final ExaminationRepository examinationRepo;
    @Autowired
    public ExaminationController(ExaminationRepository examinationRepo){
        this.examinationRepo = examinationRepo;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Examination addExamination(@RequestBody Examination examination){
        return examinationRepo.save(examination);
    }

    @DeleteMapping("/{id}")
    public void deleteExamination(@PathVariable("id") Long id){
        try{
            examinationRepo.deleteById(id);
        } catch (EmptyResultDataAccessException ignored){

        };
    }

}
