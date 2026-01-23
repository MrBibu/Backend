package com.academiago.backend.controller;

import com.academiago.backend.model.Submission;
import com.academiago.backend.repository.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions() { return ResponseEntity.ok(submissionService.getAllSubmissions()); }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Integer id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission) { return ResponseEntity.ok(submissionService.createSubmission(submission)); }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(@PathVariable Integer id, @RequestBody Submission submission) {
        return ResponseEntity.ok(submissionService.updateSubmission(id, submission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Integer id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}
