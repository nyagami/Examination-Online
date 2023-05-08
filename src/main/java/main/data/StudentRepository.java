package main.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.models.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}
