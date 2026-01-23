package com.academiago.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "programs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "faculty_id"})
        },
        indexes = {
                @Index(name = "idx_program_faculty", columnList = "faculty_id"),
                @Index(name = "idx_program_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @NotNull
    @Column(nullable = false)
    private String name;
}
