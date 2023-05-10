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

@Controller
@RequestMapping("/")
public class CourseController {
	@Autowired
	private ExaminationRepository examinationRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	UserRepository userRepository;
	@GetMapping("/teacher/course/{id}")
	public String teacherCourse(@PathVariable("id") Long id, Model model, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName());
		Teacher teacher = teacherRepository.findByUser(user);
		Course course = courseRepository.findById(id).orElseThrow();
		if(!course.getTeacher().equals(teacher)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		Iterable<Student> students = course.getStudentList();
		Iterable<Examination> examinations = examinationRepository.findByCourse(course);
		model.addAttribute("students", students);
		model.addAttribute("exams", examinations);
		model.addAttribute("teacher", teacher);
		model.addAttribute("course", course);
		return "course_teacher";
	}

	@GetMapping("/student/course/{id}")
	public String studentCourse(@PathVariable("id") Long id, Model model, Authentication authentication){
		User user = userRepository.findByUsername(authentication.getName());
		Student student = studentRepository.findByUser(user);
		Course course = courseRepository.findById(id).orElseThrow();
		if(!course.getStudentList().contains(student)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		Iterable<Student> students = course.getStudentList();
		Iterable<Examination> examinations = examinationRepository.findByCourseAndIsVisible(course, true);
		model.addAttribute("students", students);
		model.addAttribute("exams", examinations);
		model.addAttribute("teacher", course.getTeacher());
		model.addAttribute("course", course);
		return "course_student";
	}
}
