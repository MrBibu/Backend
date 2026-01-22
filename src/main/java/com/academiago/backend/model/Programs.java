package com.academiago.backend.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="Programs")
public class Programs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculties faculty;

    private String name;
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
