package com.academiago.backend.controller;

import com.academiago.backend.model.Answer;
import com.academiago.backend.repository.AnswerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerRepository answerRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@Valid @RequestBody Answer answer) {
        return ResponseEntity.ok(answerRepository.save(answer));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Answer>> getAllAnswers() {
        return ResponseEntity.ok(answerRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        return answerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(
            @PathVariable Long id,
            @Valid @RequestBody Answer updated
    ) {
        return answerRepository.findById(id)
                .map(answer -> {
                    answer.setQuestion(updated.getQuestion());
                    answer.setTeacher(updated.getTeacher());
                    answer.setText(updated.getText());
                    return ResponseEntity.ok(answerRepository.save(answer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        if (!answerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        answerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By question
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerRepository.findByQuestion_Id(questionId));
    }

    // By teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(answerRepository.findByTeacher_Id(teacherId));
    }

    // By question + teacher
    @GetMapping("/question/{questionId}/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByQuestionAndTeacher(
            @PathVariable Long questionId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(answerRepository.findByQuestion_IdAndTeacher_Id(questionId, teacherId));
    }
}
