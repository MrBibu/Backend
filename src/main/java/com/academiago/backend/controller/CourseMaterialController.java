package com.academiago.backend.controller;

import com.academiago.backend.dto.CourseMaterialDTO;
import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.CourseMaterialRepository;
import com.academiago.backend.repository.SubjectRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-materials")
@RequiredArgsConstructor
public class CourseMaterialController {

    private final CourseMaterialRepository courseMaterialRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final UsersRepository usersRepository;

    // ================= CREATE =================
    // Only TEACHER can upload course materials
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<CourseMaterial> createCourseMaterial(
            @Valid @RequestBody CourseMaterialDTO dto,
            Authentication authentication
    ) {
        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        TeacherProfile teacherProfile = teacherProfileRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (!teacherProfile.getId().equals(dto.getTeacherId())) {
            return ResponseEntity.status(403).build(); // Forbidden if teacher tries to upload as someone else
        }

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        CourseMaterial material = CourseMaterial.builder()
                .subject(subject)
                .uploadedBy(teacherProfile)
                .fileURL(dto.getFileURL())
                .build();

        return ResponseEntity.ok(courseMaterialRepository.save(material));
    }

    // ================= READ =================
    // Students, teachers, and admins can view materials
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials() {
        return ResponseEntity.ok(courseMaterialRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseMaterial> getCourseMaterialById(@PathVariable Long id) {
        return courseMaterialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    // Only the TEACHER who uploaded or ADMIN can update
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseMaterial> updateCourseMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CourseMaterialDTO dto,
            Authentication authentication
    ) {
        return (ResponseEntity<CourseMaterial>) courseMaterialRepository.findById(id)
                .map(material -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_TEACHER")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        TeacherProfile teacherProfile = teacherProfileRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

                        if (!material.getUploadedBy().getId().equals(teacherProfile.getId())) {
                            return ResponseEntity.status(403).build(); // Forbidden
                        }
                    }

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
    // Only ADMIN or the TEACHER who uploaded can delete
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseMaterial(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return (ResponseEntity<Void>) courseMaterialRepository.findById(id)
                .map(material -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_TEACHER")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        TeacherProfile teacherProfile = teacherProfileRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

                        if (!material.getUploadedBy().getId().equals(teacherProfile.getId())) {
                            return ResponseEntity.status(403).<Void>build();
                        }
                    }

                    courseMaterialRepository.delete(material);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= FILTER APIs =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<CourseMaterial>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(courseMaterialRepository.findBySubject_Id(subjectId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseMaterial>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(courseMaterialRepository.findByUploadedBy_Id(teacherId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/subject/{subjectId}/teacher/{teacherId}")
    public ResponseEntity<List<CourseMaterial>> getBySubjectAndTeacher(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(courseMaterialRepository.findBySubject_IdAndUploadedBy_Id(subjectId, teacherId));
    }
}
