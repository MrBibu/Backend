package com.academiago.backend.controller;

import com.academiago.backend.model.Submission;
import com.academiago.backend.model.SubmissionStatus;
import com.academiago.backend.repository.SubmissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionRepository submissionRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Submission> createSubmission(
            @Valid @RequestBody Submission submission
    ) {
        return ResponseEntity.ok(submissionRepository.save(submission));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(
            @PathVariable Long id,
            @Valid @RequestBody Submission updated
    ) {
        return submissionRepository.findById(id)
                .map(submission -> {
                    submission.setAssignment(updated.getAssignment());
                    submission.setStudent(updated.getStudent());
                    submission.setSubmittedAt(updated.getSubmittedAt());
                    submission.setStatus(updated.getStatus());
                    return ResponseEntity.ok(submissionRepository.save(submission));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        if (!submissionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        submissionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By assignment
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<Submission>> getByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionRepository.findByAssignment_Id(assignmentId));
    }

    // By student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Submission>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionRepository.findByStudent_Id(studentId));
    }

    // By status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Submission>> getByStatus(@PathVariable SubmissionStatus status) {
        return ResponseEntity.ok(submissionRepository.findByStatus(status));
    }

    // By assignment + student
    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    public ResponseEntity<List<Submission>> getByAssignmentAndStudent(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(submissionRepository.findByAssignment_IdAndStudent_Id(assignmentId, studentId));
    }
}
