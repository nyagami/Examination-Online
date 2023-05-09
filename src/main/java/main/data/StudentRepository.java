package main.data;

import main.models.Student;
import main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByUser(User user);
}
