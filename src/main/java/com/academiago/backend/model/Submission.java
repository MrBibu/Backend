package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "submissions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Users student;

    @Column(nullable = false, length = 2000)
    private String submission;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private Timestamp submittedAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private BigDecimal grades;

    @Column(length = 500)
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status = SubmissionStatus.PENDING;

    public enum SubmissionStatus {
        PENDING,
        GRADED,
        RESUBMITTED,
        REJECTED
    }
}
