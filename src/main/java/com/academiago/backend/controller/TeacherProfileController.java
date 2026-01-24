package com.academiago.backend.controller;

import com.academiago.backend.model.TeacherProfile;
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

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<TeacherProfile> createTeacherProfile(
            @Valid @RequestBody TeacherProfile profile
    ) {
        return ResponseEntity.ok(teacherProfileRepository.save(profile));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<TeacherProfile>> getAllTeacherProfiles() {
        return ResponseEntity.ok(teacherProfileRepository.findAll());
    }

    // READ BY ID (user_id because of @MapsId)
    @GetMapping("/{userId}")
    public ResponseEntity<TeacherProfile> getTeacherProfileByUserId(
            @PathVariable Long userId
    ) {
        return teacherProfileRepository.findByUser_Id(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<TeacherProfile> updateTeacherProfile(
            @PathVariable Long userId,
            @Valid @RequestBody TeacherProfile updated
    ) {
        return teacherProfileRepository.findByUser_Id(userId)
                .map(profile -> {
                    profile.setName(updated.getName());
                    profile.setEmployeeId(updated.getEmployeeId());
                    profile.setEmail(updated.getEmail());
                    profile.setQualifications(updated.getQualifications());
                    profile.setPermanentAddress(updated.getPermanentAddress());
                    profile.setTemporaryAddress(updated.getTemporaryAddress());
                    profile.setDob(updated.getDob());
                    profile.setContactNo(updated.getContactNo());
                    profile.setStatus(updated.getStatus());
                    profile.setGender(updated.getGender());
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

    // ================= FILTER APIs =================

    // Active teachers
    @GetMapping("/active")
    public ResponseEntity<List<TeacherProfile>> getActiveTeachers() {
        return ResponseEntity.ok(
                teacherProfileRepository.findByStatusTrue()
        );
    }
}
