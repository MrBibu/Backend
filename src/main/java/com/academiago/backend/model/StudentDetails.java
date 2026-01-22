package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "student_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    String Full_Name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private Date dob;

    private String permanentAddress;
    private String temporaryAddress;

    private Long rollNo;
    private String batch;
    private Integer semester;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Programs program;
}
