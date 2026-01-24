package com.academiago.backend.repository;

import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUser(Users user);

    Optional<StudentProfile> findByRollNumberAndProgram_Id(Long rollNumber, Long programId);

    List<StudentProfile> findByProgram_Id(Long programId);

    List<StudentProfile> findBySemester_Id(Long semesterId);

    List<StudentProfile> findByProgram_IdAndSemester_Id(Long programId, Long semesterId);
}