package main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.OnError;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Entity
@NoArgsConstructor
public class Teacher{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Course> courses;
    @OneToOne(mappedBy = "teacher")
    private User user;
}
