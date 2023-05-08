package main.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn phải có tài khoản")
    @Column(nullable = false, name = "username")
    private String username;
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