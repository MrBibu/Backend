package com.academiago.backend.controller;

import com.academiago.backend.dto.SubmissionDTO;
import com.academiago.backend.model.Submission;
import com.academiago.backend.model.SubmissionStatus;
import com.academiago.backend.repository.AssignmentRepository;
import com.academiago.backend.repository.StudentProfileRepository;
import com.academiago.backend.repository.SubmissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentProfileRepository studentRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Submission> createSubmission(@Valid @RequestBody SubmissionDTO dto) {
        Submission submission = new Submission();
        submission.setAssignment(
                assignmentRepository.findById(dto.getAssignmentId())
                        .orElseThrow(() -> new RuntimeException("Assignment not found"))
        );
        submission.setStudent(
                studentRepository.findById(dto.getStudentId())
                        .orElseThrow(() -> new RuntimeException("Student not found"))
        );
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setStatus(SubmissionStatus.SUBMITTED);

        return ResponseEntity.ok(submissionRepository.save(submission));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE (status only) =================
    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(
            @PathVariable Long id,
            @RequestBody SubmissionDTO dto
    ) {
        return submissionRepository.findById(id)
                .map(submission -> {
                    submission.setStatus(dto.getStatus());
                    return ResponseEntity.ok(submissionRepository.save(submission));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        if (!submissionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        submissionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<Submission>> getByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionRepository.findByAssignment_Id(assignmentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Submission>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionRepository.findByStudent_Id(studentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Submission>> getByStatus(@PathVariable SubmissionStatus status) {
        return ResponseEntity.ok(submissionRepository.findByStatus(status));
    }

    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    public ResponseEntity<List<Submission>> getByAssignmentAndStudent(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(submissionRepository.findByAssignment_IdAndStudent_Id(assignmentId, studentId));
    }

    @GetMapping("/assignment/{assignmentId}/student/{studentId}/status/{status}")
    public ResponseEntity<Submission> getByAssignmentStudentAndStatus(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            @PathVariable SubmissionStatus status
    ) {
        return submissionRepository
                .findByAssignment_IdAndStudent_IdAndStatus(assignmentId, studentId, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
