package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "teacher_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDetails {

    @Id
    private Integer id; // shared PK with Users

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String phone;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
}
