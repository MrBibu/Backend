package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Entity
@Table(
        name = "semesters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number", "program_id"})
        },
        indexes = {
                @Index(name = "idx_semester_program", columnList = "program_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer number;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="program_id", nullable = false)
    private Program program;
}
