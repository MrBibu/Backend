package com.academiago.backend.controller;

import com.academiago.backend.dto.AssignmentDTO;
import com.academiago.backend.model.Assignment;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.repository.AssignmentRepository;
import com.academiago.backend.repository.SubjectRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
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
    private final SubjectRepository subjectRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @Valid @RequestBody AssignmentDTO dto
    ) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        TeacherProfile teacher = teacherProfileRepository.findById(dto.getTeacherProfileId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .subject(subject)
                .teacherProfile(teacher)
                .dueDate(dto.getDueDate().atStartOfDay())
                .build();

        return ResponseEntity.ok(assignmentRepository.save(assignment));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return assignmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentDTO dto
    ) {
        return assignmentRepository.findById(id)
                .map(assignment -> {
                    Subject subject = subjectRepository.findById(dto.getSubjectId())
                            .orElseThrow(() -> new RuntimeException("Subject not found"));

                    TeacherProfile teacher = teacherProfileRepository.findById(dto.getTeacherProfileId())
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    assignment.setTitle(dto.getTitle());
                    assignment.setSubject(subject);
                    assignment.setTeacherProfile(teacher);
                    assignment.setDueDate(dto.getDueDate().atStartOfDay());

                    return ResponseEntity.ok(assignmentRepository.save(assignment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        if (!assignmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assignmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Assignment>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(
                assignmentRepository.findBySubject_Id(subjectId)
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Assignment>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(
                assignmentRepository.findByTeacherProfile_Id(teacherId)
        );
    }

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
