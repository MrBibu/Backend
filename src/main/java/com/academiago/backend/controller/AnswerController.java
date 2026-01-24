package com.academiago.backend.controller;

import com.academiago.backend.dto.AnswerDTO;
import com.academiago.backend.model.Answer;
import com.academiago.backend.model.Users;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Question;
import com.academiago.backend.repository.AnswerRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
import com.academiago.backend.repository.QuestionRepository;
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
    private final QuestionRepository questionRepository;
    private final TeacherProfileRepository teacherRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@Valid @RequestBody AnswerDTO dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
        TeacherProfile teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Answer answer = Answer.builder()
                .question(question)
                .teacher(teacher)
                .text(dto.getText())
                .build();

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
            @Valid @RequestBody AnswerDTO dto
    ) {
        return answerRepository.findById(id)
                .map(answer -> {
                    Question question = questionRepository.findById(dto.getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Question not found"));
                    TeacherProfile teacher = teacherRepository.findById(dto.getTeacherId())
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    answer.setQuestion(question);
                    answer.setTeacher(teacher);
                    answer.setText(dto.getText());

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

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerRepository.findByQuestion_Id(questionId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(answerRepository.findByTeacher_Id(teacherId));
    }

    @GetMapping("/question/{questionId}/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByQuestionAndTeacher(
            @PathVariable Long questionId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(answerRepository.findByQuestion_IdAndTeacher_Id(questionId, teacherId));
    }
}
