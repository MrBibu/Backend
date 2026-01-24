package com.academiago.backend.repository;

import com.academiago.backend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCodeAndSemester_Id(String code, Long semesterId);

    List<Subject> findBySemester_Id(Long semesterId);

    List<Subject> findByTeacherProfile_Id(Long teacherId);
}
