package com.academiago.backend.controller;

import com.academiago.backend.dto.QuestionDTO;
import com.academiago.backend.model.*;
import com.academiago.backend.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final UsersRepository usersRepository;

    // =============== CREATE ===============
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @Valid @RequestBody QuestionDTO dto,
            Authentication authentication
    ) {
        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        StudentProfile studentProfile = studentProfileRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        if (!studentProfile.getId().equals(dto.getStudentId())) {
            return ResponseEntity.status(403).build();
        }

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        TeacherProfile teacher = null;
        if (dto.getTeacherId() != null) {
            teacher = teacherProfileRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        Question question = Question.builder()
                .student(studentProfile)
                .subject(subject)
                .teacher(teacher != null ? teacher.getUser() : null)
                .text(dto.getText())
                .status(dto.getStatus())
                .build();

        return ResponseEntity.ok(questionRepository.save(question));
    }

    // =============== READ ===============
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =============== UPDATE ===============
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO dto,
            Authentication authentication
    ) {
        return questionRepository.findById(id)
                .map(question -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_STUDENT")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        StudentProfile studentProfile = studentProfileRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Student profile not found"));

                        if (!question.getStudent().getId().equals(studentProfile.getId())) {
                            return ResponseEntity.status(403).<Question>build(); // 👈 Cast to match return type
                        }
                    }

                    if (role.equals("ROLE_TEACHER")) {
                        question.setStatus(dto.getStatus());
                        return ResponseEntity.ok(questionRepository.save(question));
                    }

                    StudentProfile student = studentProfileRepository.findById(dto.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found"));
                    Subject subject = subjectRepository.findById(dto.getSubjectId())
                            .orElseThrow(() -> new RuntimeException("Subject not found"));

                    TeacherProfile teacher = null;
                    if (dto.getTeacherId() != null) {
                        teacher = teacherProfileRepository.findById(dto.getTeacherId())
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));
                    }

                    question.setStudent(student);
                    question.setSubject(subject);
                    question.setTeacher(teacher != null ? teacher.getUser() : null);
                    question.setText(dto.getText());
                    question.setStatus(dto.getStatus());

                    return ResponseEntity.ok(questionRepository.save(question));
                })
                .orElse(ResponseEntity.notFound().build()); // Matches ResponseEntity<Question>
    }


    // =============== DELETE ===============
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuestion(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return questionRepository.findById(id)
                .map(question -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_STUDENT")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        StudentProfile studentProfile = studentProfileRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Student profile not found"));

                        if (!question.getStudent().getId().equals(studentProfile.getId())) {
                            return ResponseEntity.status(403).build();
                        }
                    }

                    questionRepository.delete(question);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }





    // =============== FILTERS ===============
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Question>> getByStudent(
            @PathVariable Long studentId,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
            StudentProfile studentProfile = studentProfileRepository.findByUser(loggedInUser)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!studentProfile.getId().equals(studentId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(questionRepository.findByStudent_Id(studentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Question>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(questionRepository.findBySubject_Id(subjectId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Question>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionRepository.findByTeacher_Id(teacherId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Question>> getByStatus(@PathVariable QuestionStatus status) {
        return ResponseEntity.ok(questionRepository.findByStatus(status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/subject/{subjectId}/status/{status}")
    public ResponseEntity<List<Question>> getBySubjectAndStatus(
            @PathVariable Long subjectId,
            @PathVariable QuestionStatus status
    ) {
        return ResponseEntity.ok(questionRepository.findBySubject_IdAndStatus(subjectId, status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Question>> getByStudentAndStatus(
            @PathVariable Long studentId,
            @PathVariable QuestionStatus status,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
            StudentProfile studentProfile = studentProfileRepository.findByUser(loggedInUser)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!studentProfile.getId().equals(studentId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(questionRepository.findByStudent_IdAndStatus(studentId, status));
    }
}
