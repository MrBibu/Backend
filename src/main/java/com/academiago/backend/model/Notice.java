package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 150)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="visible_to", nullable = false, length = 20)
    private NoticeVisibility visibleTo; //ALL,STUDENTS,TEACHERS

    //optional: terget specific
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="faculty_id")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="program_id")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="semester_id")
    private Semester semester;

    @Column(nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();
}
