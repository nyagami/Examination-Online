package main.data;

import main.models.Course;
import main.models.Teacher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository <Course,Long>{
	List<Course> findByTeacher(Teacher teacher);
}
