package com.academiago.backend.repository;

import com.academiago.backend.model.Submission;
import com.academiago.backend.model.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByAssignment_Id(Long assignmentId);

    List<Submission> findByStudent_Id(Long studentId);

    List<Submission> findByStatus(SubmissionStatus status);

    List<Submission> findByAssignment_IdAndStudent_Id(Long assignmentId, Long studentId);

    Optional<Submission> findByAssignment_IdAndStudent_IdAndStatus(Long assignmentId, Long studentId, SubmissionStatus status);
}
