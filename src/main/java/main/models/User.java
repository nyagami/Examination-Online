package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn cần phải điền email")
    @Column(nullable = false)
    private String email;
    @NotNull(message = "Bạn phải đặt tên")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "Bạn cần mật khẩu để đăng nhập")
    @Size(min=6, message = "Mật khẩu của bạn cần phải có 6 ký tự")
    @Column(nullable = false)
    private String password;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    @Column(nullable = false)

    private String role;
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
}