package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@MappedSuperclass
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn phải đặt tên")
    @Column(nullable = false)
    private String name;
    private Date dateOfBirth;
    private String address;
    @NotNull(message = "Bạn cần phải điền email")
    @Column(nullable = false)
    private String email;
    private String phoneNumber;
    @NotNull(message = "Bạn cần mật khẩu để đăng nhập")
    @Size(min=6, message = "Mật khẩu của bạn cần phải có 6 ký tự")
    @Column(nullable = false)
    private String passWord;
}