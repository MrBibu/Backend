package com.academiago.backend.controller;

import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.repository.CourseMaterialRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-materials")
@RequiredArgsConstructor
public class CourseMaterialController {

    private final CourseMaterialRepository courseMaterialRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<CourseMaterial> createCourseMaterial(
            @Valid @RequestBody CourseMaterial material
    ) {
        return ResponseEntity.ok(courseMaterialRepository.save(material));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials() {
        return ResponseEntity.ok(courseMaterialRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseMaterial> getCourseMaterialById(@PathVariable Long id) {
        return courseMaterialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CourseMaterial> updateCourseMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CourseMaterial updated
    ) {
        return courseMaterialRepository.findById(id)
                .map(material -> {
                    material.setSubject(updated.getSubject());
                    material.setUploadedBy(updated.getUploadedBy());
                    material.setFileURL(updated.getFileURL());
                    return ResponseEntity.ok(courseMaterialRepository.save(material));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseMaterial(@PathVariable Long id) {
        if (!courseMaterialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseMaterialRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By subject
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<CourseMaterial>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(courseMaterialRepository.findBySubject_Id(subjectId));
    }

    // By teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseMaterial>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(courseMaterialRepository.findByUploadedBy_Id(teacherId));
    }

    // By subject + teacher
    @GetMapping("/subject/{subjectId}/teacher/{teacherId}")
    public ResponseEntity<List<CourseMaterial>> getBySubjectAndTeacher(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                courseMaterialRepository.findBySubject_IdAndUploadedBy_Id(subjectId, teacherId)
        );
    }
}
