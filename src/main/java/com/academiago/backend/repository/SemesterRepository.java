package com.academiago.backend.repository;

import com.academiago.backend.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByProgram_Id(Long programId);

    Optional<Semester> findByNumberAndProgram_Id(Integer number, Long programId);
}
