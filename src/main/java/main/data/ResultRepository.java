package main.data;

import main.models.Examination;
import main.models.Result;
import main.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    public Iterable<Result> findByExamination(Examination examination);
    Result findByExaminationAndStudent(Examination examination, Student student);
    Iterable<Result> findByExaminationAndDone(Examination examination, Boolean done);
}
