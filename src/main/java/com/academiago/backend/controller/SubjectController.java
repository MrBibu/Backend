package com.academiago.backend.controller;

import com.academiago.backend.dto.SubjectDTO;
import com.academiago.backend.model.Semester;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.repository.SemesterRepository;
import com.academiago.backend.repository.SubjectRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
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
    private final SemesterRepository semesterRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Subject> createSubject(
            @Valid @RequestBody SubjectDTO dto
    ) {
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        TeacherProfile teacher = teacherProfileRepository.findById(dto.getTeacherProfileId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = Subject.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .semester(semester)
                .teacherProfile(teacher)
                .build();

        return ResponseEntity.ok(subjectRepository.save(subject));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectDTO dto
    ) {
        return subjectRepository.findById(id)
                .map(subject -> {
                    subject.setCode(dto.getCode());
                    subject.setName(dto.getName());
                    subject.setSemester(
                            semesterRepository.findById(dto.getSemesterId())
                                    .orElseThrow(() -> new RuntimeException("Semester not found"))
                    );
                    subject.setTeacherProfile(
                            teacherProfileRepository.findById(dto.getTeacherProfileId())
                                    .orElseThrow(() -> new RuntimeException("Teacher not found"))
                    );
                    return ResponseEntity.ok(subjectRepository.save(subject));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        if (!subjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        subjectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<Subject>> getSubjectsBySemester(
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                subjectRepository.findBySemester_Id(semesterId)
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Subject>> getSubjectsByTeacher(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                subjectRepository.findByTeacherProfile_Id(teacherId)
        );
    }

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
