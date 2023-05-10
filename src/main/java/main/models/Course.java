package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Random;

@Data
@Entity
@NoArgsConstructor
public class Course {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Lớp học cần phải có tên")
    private String name;
    @Column(unique = true)
    private String code;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Student.class)
    private List<Student> studentList;
    @ManyToOne(targetEntity = Teacher.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Lớp học cần có giảng viên")
    private Teacher teacher;
    @PrePersist
    void code(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        this.code=sb.toString();
    }
}
