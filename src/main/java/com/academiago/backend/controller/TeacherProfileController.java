package com.academiago.backend.controller;

import com.academiago.backend.dto.TeacherProfileDTO;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.TeacherProfileRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-profiles")
@RequiredArgsConstructor
public class TeacherProfileController {

    private final TeacherProfileRepository teacherProfileRepository;
    private final UsersRepository usersRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<TeacherProfile> createTeacherProfile(
            @Valid @RequestBody TeacherProfileDTO dto
    ) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeacherProfile profile = TeacherProfile.builder()
                .user(user)
                .name(dto.getName())
                .employeeId(dto.getEmployeeId())
                .email(dto.getEmail())
                .qualifications(dto.getQualifications())
                .permanentAddress(dto.getPermanentAddress())
                .temporaryAddress(dto.getTemporaryAddress())
                .dob(dto.getDob())
                .contactNo(dto.getContactNo())
                .gender(dto.getGender())
                .status(dto.getStatus())
                .build();

        return ResponseEntity.ok(teacherProfileRepository.save(profile));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<TeacherProfile>> getAllTeacherProfiles() {
        return ResponseEntity.ok(teacherProfileRepository.findAll());
    }

    // READ BY USER ID
    @GetMapping("/{userId}")
    public ResponseEntity<TeacherProfile> getByUserId(@PathVariable Long userId) {
        return teacherProfileRepository.findByUser_Id(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<TeacherProfile> updateTeacherProfile(
            @PathVariable Long userId,
            @Valid @RequestBody TeacherProfileDTO dto
    ) {
        return teacherProfileRepository.findByUser_Id(userId)
                .map(profile -> {
                    profile.setName(dto.getName());
                    profile.setEmployeeId(dto.getEmployeeId());
                    profile.setEmail(dto.getEmail());
                    profile.setQualifications(dto.getQualifications());
                    profile.setPermanentAddress(dto.getPermanentAddress());
                    profile.setTemporaryAddress(dto.getTemporaryAddress());
                    profile.setDob(dto.getDob());
                    profile.setContactNo(dto.getContactNo());
                    profile.setGender(dto.getGender());
                    profile.setStatus(dto.getStatus());
                    return ResponseEntity.ok(teacherProfileRepository.save(profile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteTeacherProfile(@PathVariable Long userId) {
        return teacherProfileRepository.findByUser_Id(userId)
                .map(profile -> {
                    teacherProfileRepository.delete(profile);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // FILTER
    @GetMapping("/active")
    public ResponseEntity<List<TeacherProfile>> getActiveTeachers() {
        return ResponseEntity.ok(teacherProfileRepository.findByStatusTrue());
    }
}
