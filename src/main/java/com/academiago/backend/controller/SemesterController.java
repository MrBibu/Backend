package com.academiago.backend.controller;

import com.academiago.backend.model.Semester;
import com.academiago.backend.repository.SemesterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterRepository semesterRepository;

    // ================= CREATE =================
    // Only ADMIN can create semesters
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Semester> createSemester(@Valid @RequestBody Semester semester) {
        return ResponseEntity.ok(semesterRepository.save(semester));
    }

    // ================= READ =================
    // Everyone can view semesters
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters() {
        return ResponseEntity.ok(semesterRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable Long id) {
        return semesterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    // Only ADMIN can update semesters
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@PathVariable Long id, @Valid @RequestBody Semester updated) {
        return semesterRepository.findById(id)
                .map(semester -> {
                    semester.setNumber(updated.getNumber());
                    semester.setProgram(updated.getProgram());
                    return ResponseEntity.ok(semesterRepository.save(semester));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    // Only ADMIN can delete semesters
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        if (!semesterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        semesterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTERS =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Semester>> getSemestersByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(semesterRepository.findByProgram_Id(programId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/program/{programId}/number/{number}")
    public ResponseEntity<Semester> getSemesterByNumberAndProgram(
            @PathVariable Long programId,
            @PathVariable Integer number
    ) {
        return semesterRepository.findByNumberAndProgram_Id(number, programId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
