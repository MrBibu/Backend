package com.academiago.backend.repository;

import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.Program;
import com.academiago.backend.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    List<StudentProfile> findByProgram(Program program);
    List<StudentProfile> findBySemester(Semester semester);
}
