package com.academiago.backend.controller;

import com.academiago.backend.dto.SubmissionDTO;
import com.academiago.backend.model.Submission;
import com.academiago.backend.model.SubmissionStatus;
import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.AssignmentRepository;
import com.academiago.backend.repository.StudentProfileRepository;
import com.academiago.backend.repository.SubmissionRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentProfileRepository studentRepository;
    private final UsersRepository usersRepository;

    // ================= CREATE =================
    // Only STUDENT can submit, and only for themselves
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<Submission> createSubmission(
            @Valid @RequestBody SubmissionDTO dto,
            Authentication authentication
    ) {
        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        StudentProfile studentProfile = studentRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        if (!studentProfile.getId().equals(dto.getStudentId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        Submission submission = new Submission();
        submission.setAssignment(
                assignmentRepository.findById(dto.getAssignmentId())
                        .orElseThrow(() -> new RuntimeException("Assignment not found"))
        );
        submission.setStudent(studentProfile);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setStatus(SubmissionStatus.SUBMITTED);

        return ResponseEntity.ok(submissionRepository.save(submission));
    }

    // ================= READ =================
    // ADMIN can view all, TEACHER can view by assignment, STUDENT can view their own
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE (status only) =================
    // Only TEACHER or ADMIN can update status
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(
            @PathVariable Long id,
            @RequestBody SubmissionDTO dto
    ) {
        return submissionRepository.findById(id)
                .map(submission -> {
                    submission.setStatus(dto.getStatus());
                    return ResponseEntity.ok(submissionRepository.save(submission));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    // Only ADMIN or the STUDENT who submitted can delete
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return (ResponseEntity<Void>) submissionRepository.findById(id)
                .map(submission -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if ("ROLE_STUDENT".equals(role)) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        StudentProfile studentProfile = studentRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Student profile not found"));

                        if (!submission.getStudent().getId().equals(studentProfile.getId())) {
                            // Explicit cast to ResponseEntity<Void> using .<Void>build()
                            return ResponseEntity.status(403).<Void>build();
                        }
                    }

                    submissionRepository.delete(submission);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // ================= FILTER APIs =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<Submission>> getByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionRepository.findByAssignment_Id(assignmentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Submission>> getByStudent(
            @PathVariable Long studentId,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
            StudentProfile studentProfile = studentRepository.findByUser(loggedInUser)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!studentProfile.getId().equals(studentId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(submissionRepository.findByStudent_Id(studentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Submission>> getByStatus(@PathVariable SubmissionStatus status) {
        return ResponseEntity.ok(submissionRepository.findByStatus(status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    public ResponseEntity<List<Submission>> getByAssignmentAndStudent(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
            StudentProfile studentProfile = studentRepository.findByUser(loggedInUser)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!studentProfile.getId().equals(studentId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(submissionRepository.findByAssignment_IdAndStudent_Id(assignmentId, studentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/assignment/{assignmentId}/student/{studentId}/status/{status}")
    public ResponseEntity<Submission> getByAssignmentStudentAndStatus(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            @PathVariable SubmissionStatus status,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
            StudentProfile studentProfile = studentRepository.findByUser(loggedInUser)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!studentProfile.getId().equals(studentId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return submissionRepository
                .findByAssignment_IdAndStudent_IdAndStatus(assignmentId, studentId, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
