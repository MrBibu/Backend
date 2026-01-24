package com.academiago.backend.controller;

import com.academiago.backend.model.Assignment;
import com.academiago.backend.repository.AssignmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @Valid @RequestBody Assignment assignment
    ) {
        return ResponseEntity.ok(assignmentRepository.save(assignment));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return assignmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody Assignment updated
    ) {
        return assignmentRepository.findById(id)
                .map(assignment -> {
                    assignment.setTitle(updated.getTitle());
                    assignment.setSubject(updated.getSubject());
                    assignment.setTeacherProfile(updated.getTeacherProfile());
                    assignment.setDueDate(updated.getDueDate());
                    return ResponseEntity.ok(assignmentRepository.save(assignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        if (!assignmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assignmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By subject
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Assignment>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(assignmentRepository.findBySubject_Id(subjectId));
    }

    // By teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Assignment>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(assignmentRepository.findByTeacherProfile_Id(teacherId));
    }

    // By subject + teacher
    @GetMapping("/subject/{subjectId}/teacher/{teacherId}")
    public ResponseEntity<List<Assignment>> getBySubjectAndTeacher(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                assignmentRepository.findBySubject_IdAndTeacherProfile_Id(subjectId, teacherId)
        );
    }
}
