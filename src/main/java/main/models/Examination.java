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
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title = "Chưa đặt tên";
    private String description = "Bài kiểm tra";
    @ManyToOne(targetEntity = Course.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Bài kiểm tra phải có lớp")
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
