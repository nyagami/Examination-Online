package main.data;

import main.models.Course;
import main.models.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    Examination findByCourseAndIsVisible(Course course, Boolean visible);
}
