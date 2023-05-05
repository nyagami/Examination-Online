package main.data;

import main.models.Examination;
import main.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    public Iterable<Result> findByExamination(Examination examination);
}
