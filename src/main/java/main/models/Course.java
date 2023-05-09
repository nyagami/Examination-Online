package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Lớp học cần phải có tên")
    private String name;

    private String code;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Student.class)
    private List<Student> studentList;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Student.class)
    private List<Student> studentQueue;
    @ManyToOne(targetEntity = Teacher.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Lớp học cần có giảng viên")
    private Teacher teacher;
    @PrePersist
    void code(){
        // auto gen code format C000001
        this.code = String.format("C%06d", this.id);
    }
}
