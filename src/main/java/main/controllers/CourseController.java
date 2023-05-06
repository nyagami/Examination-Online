package main.controllers;

import main.data.CourseRepository;
import main.data.ExaminationRepository;
import main.models.Course;
import main.models.Examination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private ExaminationRepository examinationRepository;

	@Autowired
	private CourseRepository courseRepository;
	@GetMapping("/student/{courseId}")
	public String course(@PathVariable("courseId") Long id, Model model) {
		Optional<Course> course = courseRepository.findById(id);
		if(course.isPresent()){
			List<Examination> examinations = (List<Examination>) examinationRepository.findByCourseAndIsVisible(course.get(),true);
			model.addAttribute("examinations",examinations);
		}
		return "course.html";
	}
}
