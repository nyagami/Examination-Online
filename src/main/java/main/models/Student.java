package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn phải có tài khoản")
    @OneToOne(targetEntity = User.class, cascade = CascadeType.REMOVE)
    private User user;
    private String email;
    @NotNull(message = "Bạn phải đặt tên")
    @Column(nullable = false)
    private String name;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
}
