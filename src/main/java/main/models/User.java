package main.models;

import jakarta.persistence.*;
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
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull(message = "Bạn cần mật khẩu để đăng nhập")
    @Size(min=6, message = "Mật khẩu của bạn cần phải có 6 ký tự")
    @Column(nullable = false)
    private String password;
    private String role;
}