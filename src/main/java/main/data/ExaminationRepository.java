package main.data;

import main.models.Course;
import main.models.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    Iterable<Examination> findByCourseAndIsVisible(Course course, Boolean visible);
    Iterable<Examination> findByCourse(Course course);
}
