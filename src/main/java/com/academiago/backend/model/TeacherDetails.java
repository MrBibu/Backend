package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "teacher_details")
public class TeacherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    private String name;
    private String employeeId;
    private String email;
    private String qualifications;
    private String permanentAddress;
    private String temporaryAddress;
    private Date dob;
    private String contactNo;
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
