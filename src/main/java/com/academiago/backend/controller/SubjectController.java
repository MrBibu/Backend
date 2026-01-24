package com.academiago.backend.controller;

import com.academiago.backend.model.Subject;
import com.academiago.backend.repository.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectRepository subjectRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Subject> createSubject(
            @Valid @RequestBody Subject subject
    ) {
        return ResponseEntity.ok(subjectRepository.save(subject));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody Subject updatedSubject
    ) {
        return subjectRepository.findById(id)
                .map(subject -> {
                    subject.setCode(updatedSubject.getCode());
                    subject.setName(updatedSubject.getName());
                    subject.setSemester(updatedSubject.getSemester());
                    subject.setTeacherProfile(updatedSubject.getTeacherProfile());
                    return ResponseEntity.ok(subjectRepository.save(subject));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        if (!subjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        subjectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // Find by semester
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<Subject>> getSubjectsBySemester(
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                subjectRepository.findBySemester_Id(semesterId)
        );
    }

    // Find by teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Subject>> getSubjectsByTeacher(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                subjectRepository.findByTeacherProfile_Id(teacherId)
        );
    }

    // Find by code + semester (unique)
    @GetMapping("/code/{code}/semester/{semesterId}")
    public ResponseEntity<Subject> getByCodeAndSemester(
            @PathVariable String code,
            @PathVariable Long semesterId
    ) {
        return subjectRepository.findByCodeAndSemester_Id(code, semesterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
