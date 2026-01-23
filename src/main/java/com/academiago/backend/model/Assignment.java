package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "assignments",
        indexes = {
                @Index(name = "idx_assignment_subject", columnList = "subject_id"),
                @Index(name = "idx_assignment_teacher", columnList = "teacher_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 150)
    private String title;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject_id", nullable = false, unique = true)
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacher_id", nullable = false, unique = true)
    private TeacherProfile teacherProfile;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime dueDate;
}
