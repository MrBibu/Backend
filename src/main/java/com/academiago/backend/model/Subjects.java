package com.project.Project.model;

import com.academiago.backend.model.Programs;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subjects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private Integer semester;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Programs program;

}