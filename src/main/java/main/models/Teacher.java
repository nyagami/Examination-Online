package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Entity
@NoArgsConstructor
public class Teacher{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn phải có tài khoản")
    @Column(nullable = false, name = "username", unique = true)
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
}
