package main.data;

import main.models.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    public List<Student> findAll(Sort sort);
    public Student findByName(String name);
}
