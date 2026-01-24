package com.academiago.backend.controller;

import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.StudentProfileRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileRepository studentProfileRepository;
    private final UsersRepository usersRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<StudentProfile> createStudentProfile(
            @Valid @RequestBody StudentProfile profile
    ) {
        return ResponseEntity.ok(studentProfileRepository.save(profile));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<StudentProfile>> getAllStudentProfiles() {
        return ResponseEntity.ok(studentProfileRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentProfile> getStudentProfileById(
            @PathVariable Long id
    ) {
        return studentProfileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<StudentProfile> updateStudentProfile(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfile updated
    ) {
        return studentProfileRepository.findById(id)
                .map(profile -> {
                    profile.setFullName(updated.getFullName());
                    profile.setGender(updated.getGender());
                    profile.setDateOfBirth(updated.getDateOfBirth());
                    profile.setPermanentAddress(updated.getPermanentAddress());
                    profile.setTemporaryAddress(updated.getTemporaryAddress());
                    profile.setRollNumber(updated.getRollNumber());
                    profile.setBatchYear(updated.getBatchYear());
                    profile.setProgram(updated.getProgram());
                    profile.setSemester(updated.getSemester());
                    return ResponseEntity.ok(studentProfileRepository.save(profile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentProfile(@PathVariable Long id) {
        if (!studentProfileRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentProfileRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // Find by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentProfile> getByUserId(
            @PathVariable Long userId
    ) {
        return usersRepository.findById(userId)
                .flatMap(studentProfileRepository::findByUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find by program
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<StudentProfile>> getByProgram(
            @PathVariable Long programId
    ) {
        return ResponseEntity.ok(
                studentProfileRepository.findByProgram_Id(programId)
        );
    }

    // Find by semester
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<StudentProfile>> getBySemester(
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                studentProfileRepository.findBySemester_Id(semesterId)
        );
    }

    // Find by program + semester
    @GetMapping("/program/{programId}/semester/{semesterId}")
    public ResponseEntity<List<StudentProfile>> getByProgramAndSemester(
            @PathVariable Long programId,
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                studentProfileRepository.findByProgram_IdAndSemester_Id(programId, semesterId)
        );
    }

    // Find by rollNo + program (unique)
    @GetMapping("/roll/{rollNo}/program/{programId}")
    public ResponseEntity<StudentProfile> getByRollNoAndProgram(
            @PathVariable Long rollNo,
            @PathVariable Long programId
    ) {
        return studentProfileRepository
                .findByRollNoAndProgram_Id(rollNo, programId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
