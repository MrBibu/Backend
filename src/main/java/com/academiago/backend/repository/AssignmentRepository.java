package com.academiago.backend.repository;

import com.academiago.backend.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubject_Id(Long subjectId);

    List<Assignment> findByTeacherProfile_Id(Long teacherId);

    List<Assignment> findBySubject_IdAndTeacherProfile_Id(Long subjectId, Long teacherId);
}
