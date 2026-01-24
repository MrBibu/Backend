package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "teacher_profile")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
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
    @Column(name = "created_at",nullable = false)
    private Timestamp createdAt;
}
