package main.controllers;

import main.data.ExaminationRepository;
import main.data.ResultRepository;
import main.models.Examination;
import main.models.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/examination")
public class ExaminationController {
    private final ExaminationRepository examinationRepo;
    private final ResultRepository resultRepo;
    @Autowired
    public ExaminationController(ExaminationRepository examinationRepo, ResultRepository resultRepo){
        this.examinationRepo = examinationRepo;
        this.resultRepo = resultRepo;
    }

    @GetMapping("/{id}")
    public String examination(@PathVariable("id") Long id, Model model){
        System.out.println(id);
        Examination examination = examinationRepo.getReferenceById(id);
        Iterable<Result> results = resultRepo.findByExamination(examination);
        model.addAttribute("examination", examination);
        model.addAttribute("results", results);
        return "examination";
    }
}
