package com.academiago.backend.controller;

import com.academiago.backend.dto.QuestionDTO;
import com.academiago.backend.model.*;
import com.academiago.backend.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @Valid @RequestBody QuestionDTO dto
    ) {
        StudentProfile student = studentProfileRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        TeacherProfile teacher = null;
        if (dto.getTeacherId() != null) {
            teacher = teacherProfileRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        assert teacher != null;
        Question question = Question.builder()
                .student(student)
                .subject(subject)
                .teacher(teacher.getUser())
                .text(dto.getText())
                .status(dto.getStatus())
                .build();

        return ResponseEntity.ok(questionRepository.save(question));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO dto
    ) {
        return questionRepository.findById(id)
                .map(question -> {
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
                    assert teacher != null;
                    question.setTeacher(teacher.getUser());
                    question.setText(dto.getText());
                    question.setStatus(dto.getStatus());

                    return ResponseEntity.ok(questionRepository.save(question));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (!questionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Question>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(
                questionRepository.findByStudent_Id(studentId)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Question>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(
                questionRepository.findBySubject_Id(subjectId)
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Question>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(
                questionRepository.findByTeacher_Id(teacherId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Question>> getByStatus(@PathVariable QuestionStatus status) {
        return ResponseEntity.ok(
                questionRepository.findByStatus(status)
        );
    }

    @GetMapping("/subject/{subjectId}/status/{status}")
    public ResponseEntity<List<Question>> getBySubjectAndStatus(
            @PathVariable Long subjectId,
            @PathVariable QuestionStatus status
    ) {
        return ResponseEntity.ok(
                questionRepository.findBySubject_IdAndStatus(subjectId, status)
        );
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Question>> getByStudentAndStatus(
            @PathVariable Long studentId,
            @PathVariable QuestionStatus status
    ) {
        return ResponseEntity.ok(
                questionRepository.findByStudent_IdAndStatus(studentId, status)
        );
    }
}
