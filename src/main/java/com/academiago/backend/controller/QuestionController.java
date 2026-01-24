package com.academiago.backend.controller;

import com.academiago.backend.model.Question;
import com.academiago.backend.model.QuestionStatus;
import com.academiago.backend.repository.QuestionRepository;
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

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @Valid @RequestBody Question question
    ) {
        return ResponseEntity.ok(questionRepository.save(question));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody Question updated
    ) {
        return questionRepository.findById(id)
                .map(question -> {
                    question.setStudent(updated.getStudent());
                    question.setSubject(updated.getSubject());
                    question.setTeacher(updated.getTeacher());
                    question.setText(updated.getText());
                    question.setStatus(updated.getStatus());
                    return ResponseEntity.ok(questionRepository.save(question));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (!questionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Question>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(questionRepository.findByStudent_Id(studentId));
    }

    // By subject
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Question>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(questionRepository.findBySubject_Id(subjectId));
    }

    // By teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Question>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionRepository.findByTeacher_Id(teacherId));
    }

    // By status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Question>> getByStatus(@PathVariable QuestionStatus status) {
        return ResponseEntity.ok(questionRepository.findByStatus(status));
    }

    // By subject + status
    @GetMapping("/subject/{subjectId}/status/{status}")
    public ResponseEntity<List<Question>> getBySubjectAndStatus(
            @PathVariable Long subjectId,
            @PathVariable QuestionStatus status
    ) {
        return ResponseEntity.ok(questionRepository.findBySubject_IdAndStatus(subjectId, status));
    }

    // By student + status
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Question>> getByStudentAndStatus(
            @PathVariable Long studentId,
            @PathVariable QuestionStatus status
    ) {
        return ResponseEntity.ok(questionRepository.findByStudent_IdAndStatus(studentId, status));
    }
}
