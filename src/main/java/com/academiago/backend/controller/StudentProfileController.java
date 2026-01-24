package com.academiago.backend.controller;

import com.academiago.backend.dto.StudentProfileDTO;
import com.academiago.backend.model.*;
import com.academiago.backend.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileRepository studentProfileRepository;
    private final UsersRepository usersRepository;
    private final ProgramRepository programRepository;
    private final SemesterRepository semesterRepository;

    // CREATE (only ADMIN can create student profiles)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StudentProfile> createStudentProfile(
            @Valid @RequestBody StudentProfileDTO dto
    ) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found"));
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .fullName(dto.getFullName())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .permanentAddress(dto.getPermanentAddress())
                .temporaryAddress(dto.getTemporaryAddress())
                .rollNumber(dto.getRollNumber())
                .batchYear(dto.getBatchYear())
                .program(program)
                .semester(semester)
                .build();

        return ResponseEntity.ok(studentProfileRepository.save(profile));
    }

    // UPDATE (only ADMIN or the STUDENT themselves can update their profile)
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentProfile> updateStudentProfile(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfileDTO dto
    ) {
        return studentProfileRepository.findById(id)
                .map(profile -> {
                    profile.setFullName(dto.getFullName());
                    profile.setGender(dto.getGender());
                    profile.setDateOfBirth(dto.getDateOfBirth());
                    profile.setPermanentAddress(dto.getPermanentAddress());
                    profile.setTemporaryAddress(dto.getTemporaryAddress());
                    profile.setRollNumber(dto.getRollNumber());
                    profile.setBatchYear(dto.getBatchYear());
                    profile.setProgram(programRepository.findById(dto.getProgramId())
                            .orElseThrow(() -> new RuntimeException("Program not found")));
                    profile.setSemester(semesterRepository.findById(dto.getSemesterId())
                            .orElseThrow(() -> new RuntimeException("Semester not found")));
                    return ResponseEntity.ok(studentProfileRepository.save(profile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // READ BY USER ID (ADMIN can view any, STUDENT can only view their own)
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentProfile> getByUserId(@PathVariable Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return studentProfileRepository.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // FILTER APIs (ADMIN and TEACHER can query by program/semester)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<StudentProfile>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(studentProfileRepository.findByProgram_Id(programId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<StudentProfile>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(studentProfileRepository.findBySemester_Id(semesterId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/program/{programId}/semester/{semesterId}")
    public ResponseEntity<List<StudentProfile>> getByProgramAndSemester(
            @PathVariable Long programId,
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                studentProfileRepository.findByProgram_IdAndSemester_Id(programId, semesterId)
        );
    }

    // STUDENT can query their own roll + program, ADMIN can query any
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/roll/{rollNumber}/program/{programId}")
    public ResponseEntity<StudentProfile> getByRollAndProgram(
            @PathVariable Long rollNumber,
            @PathVariable Long programId
    ) {
        return studentProfileRepository
                .findByRollNumberAndProgram_Id(rollNumber, programId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
