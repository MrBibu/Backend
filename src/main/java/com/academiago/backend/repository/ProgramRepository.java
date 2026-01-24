package com.academiago.backend.repository;

import com.academiago.backend.model.Faculty;
import com.academiago.backend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByFaculty_Id(Long facultyId);

    Optional<Program> findByNameAndFaculty_Id(String name, Long facultyId);
}
