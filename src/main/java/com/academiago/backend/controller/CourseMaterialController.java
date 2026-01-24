package com.academiago.backend.controller;

import com.academiago.backend.dto.CourseMaterialDTO;
import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.repository.CourseMaterialRepository;
import com.academiago.backend.repository.SubjectRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
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
    private final SubjectRepository subjectRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<CourseMaterial> createCourseMaterial(
            @Valid @RequestBody CourseMaterialDTO dto
    ) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        TeacherProfile teacher = teacherProfileRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        CourseMaterial material = CourseMaterial.builder()
                .subject(subject)
                .uploadedBy(teacher)
                .fileURL(dto.getFileURL())
                .build();

        return ResponseEntity.ok(courseMaterialRepository.save(material));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials() {
        return ResponseEntity.ok(courseMaterialRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseMaterial> getCourseMaterialById(@PathVariable Long id) {
        return courseMaterialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<CourseMaterial> updateCourseMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CourseMaterialDTO dto
    ) {
        return courseMaterialRepository.findById(id)
                .map(material -> {
                    Subject subject = subjectRepository.findById(dto.getSubjectId())
                            .orElseThrow(() -> new RuntimeException("Subject not found"));

                    TeacherProfile teacher = teacherProfileRepository.findById(dto.getTeacherId())
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    material.setSubject(subject);
                    material.setUploadedBy(teacher);
                    material.setFileURL(dto.getFileURL());

                    return ResponseEntity.ok(courseMaterialRepository.save(material));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseMaterial(@PathVariable Long id) {
        if (!courseMaterialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseMaterialRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<CourseMaterial>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(
                courseMaterialRepository.findBySubject_Id(subjectId)
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseMaterial>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(
                courseMaterialRepository.findByUploadedBy_Id(teacherId)
        );
    }

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
