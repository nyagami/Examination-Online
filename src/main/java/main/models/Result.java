package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Student.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Student student;
    @ManyToOne(targetEntity = Examination.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Examination examination;
    private Integer numberOfCorrectAnswers = 0;
}
