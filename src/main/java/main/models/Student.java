package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Bạn phải có tài khoản")
    @OneToOne(targetEntity = User.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private String email;
    @NotNull(message = "Bạn phải đặt tên")
    @Column(nullable = false)
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
}
