package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

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
    private Date startTime;
    private Integer numberOfCorrectAnswers = 0;
    private Boolean done = false;
    @PrePersist
    void startTime(){
        this.startTime = new Date();
    }
}
