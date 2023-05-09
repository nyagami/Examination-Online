package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Student.class, cascade = CascadeType.REMOVE)
    @NotNull
    private Student student;
    @ManyToOne(targetEntity = Examination.class, cascade = CascadeType.REMOVE)
    @NotNull
    private Examination examination;
    private Integer numberOfCorrectAnswers = 0;
}
