package com.academiago.backend.repository;

import com.academiago.backend.model.Program;
import com.academiago.backend.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByFaculty(Faculty faculty);
}
