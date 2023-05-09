package main.data;

import main.models.Examination;
import main.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Iterable<Question> findByExamination(Examination examination);
}
