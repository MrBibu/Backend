package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "questions", indexes = {
        @Index(name = "idx_question_student", columnList = "student_id"),
        @Index(name = "idx_question_teacher", columnList = "teacher_id"),
        @Index(name = "idx_question_assignment", columnList = "assignment_id"),
        @Index(name = "idx_question_answered", columnList = "isAnswered")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Student who asked the question
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Users student;

    // Teacher who answers (optional until assigned)
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Users teacher;

    // Assignment this question belongs to
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @NotBlank
    @Size(max = 1000)
    @Column(nullable = false, length = 1000)
    private String questionText;

    @Column(nullable = false)
    private Boolean Answered = false;

    @Column(length = 2000)
    private String answerText;

    private Timestamp answeredAt;

    @CreationTimestamp
    private Timestamp createdAt;

    //Ownership helpers
    public Integer getStudentId() {
        return student != null ? student.getId() : null;
    }

    public Integer getTeacherId() {
        return teacher != null ? teacher.getId() : null;
    }
}
