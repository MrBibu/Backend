package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "student_profile", indexes = {
        @Index(name="idx_student_program", columnList = "semester_id")},
uniqueConstraints = {
@UniqueConstraint(columnNames = "user_id"),
@UniqueConstraint(columnNames = {"roll_number", "program_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @NotNull
           @Column(name="Full_Name", nullable = false)
    String Full_Name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotNull
    @Column(name="Date_Of_Birth", nullable = false)
    private Date dob;

    @NotNull
    @Column(name="Permannent_Address", nullable = false)
    private String permanentAddress;

    @NotNull
    @Column(name="Temporary_Address", nullable = false)
    private String temporaryAddress;

    @Column(name="roo_number", unique = true)
    private Long rollNo;

    @NotNull
    @Column(name="batch_year", nullable = false)
    private String batch;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @NotNull
    @ManyToOne
    @JoinColumn(name="semester_id", nullable = false)
    private Semester semester;
}
