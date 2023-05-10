package main.controllers;

import main.data.CourseRepository;
import main.data.ExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CourseController {

	@Autowired
	private ExaminationRepository examinationRepository;

	@Autowired
	private CourseRepository courseRepository;
	@GetMapping("/teacher/course/{id}")
	public String teacherCourse(@PathVariable("id") Long id, Model model, Authentication authentication) {
		return "course_teacher";
	}

	@GetMapping("/student/course/{id}")
	public String studentCourse(@PathVariable("id") Long id, Model model, Authentication authentication){
		return "course_student";
	}
}
