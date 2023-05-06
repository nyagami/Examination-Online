package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title = "Chưa đặt tên";
    private String description = "Bài kiểm tra";
    @ManyToMany(targetEntity = Question.class)
    private List<Question> questions;
    @ManyToOne(targetEntity = Course.class)
    private Course course;
    @NotNull(message = "Hãy thêm ngày bắt đầu")
    @Column(nullable = false)
    private Date startDate;
    @NotNull(message = "Hãy thêm ngày kết thúc")
    @Column(nullable = false)
    private Date endDate;

    @NotNull
    private Long totalTime = (long) 60; // in seconds
    @Column(nullable = false)
    @NotNull(message = "Bài kiểm tra phải có điểm")
    private Float points;
    private Boolean isVisible = false;
}
