package com.academiago.backend.repository;

import com.academiago.backend.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findByStudentId(Integer studentId);
    List<Submission> findByAssignmentId(Integer assignmentId);
}
