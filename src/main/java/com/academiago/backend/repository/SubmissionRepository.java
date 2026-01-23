package com.academiago.backend.repository;

import com.academiago.backend.model.Submission;
import com.academiago.backend.model.Assignment;
import com.academiago.backend.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent(StudentProfile student);
    List<Submission> findByAssignment(Assignment assignment);
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, StudentProfile student);
}
