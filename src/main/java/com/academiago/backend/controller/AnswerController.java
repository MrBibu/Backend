package com.academiago.backend.controller;

import com.academiago.backend.dto.AnswerDTO;
import com.academiago.backend.model.Answer;
import com.academiago.backend.model.Question;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.AnswerRepository;
import com.academiago.backend.repository.QuestionRepository;
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
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final TeacherProfileRepository teacherRepository;
    private final UsersRepository usersRepository;

    // ================= CREATE =================
    // Only TEACHER can create answers
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<Answer> createAnswer(
            @Valid @RequestBody AnswerDTO dto,
            Authentication authentication
    ) {
        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        TeacherProfile teacherProfile = teacherRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (!teacherProfile.getId().equals(dto.getTeacherId())) {
            return ResponseEntity.status(403).build(); // Forbidden if teacher tries to answer as someone else
        }

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = Answer.builder()
                .question(question)
                .teacher(teacherProfile)
                .text(dto.getText())
                .build();

        return ResponseEntity.ok(answerRepository.save(answer));
    }

    // ================= READ =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<Answer>> getAllAnswers() {
        return ResponseEntity.ok(answerRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        return answerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    // Only TEACHER who created the answer or ADMIN can update
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(
            @PathVariable Long id,
            @Valid @RequestBody AnswerDTO dto,
            Authentication authentication
    ) {
        return (ResponseEntity<Answer>) answerRepository.findById(id)
                .map(answer -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_TEACHER")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        TeacherProfile teacherProfile = teacherRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

                        if (!answer.getTeacher().getId().equals(teacherProfile.getId())) {
                            return ResponseEntity.status(403).build(); // Forbidden
                        }
                    }

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

    // ================= DELETE =================
    // Only ADMIN or the TEACHER who created the answer can delete
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return (ResponseEntity<Void>) answerRepository.findById(id)
                .map(answer -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (role.equals("ROLE_TEACHER")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
                        TeacherProfile teacherProfile = teacherRepository.findByUser(loggedInUser)
                                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

                        if (!answer.getTeacher().getId().equals(teacherProfile.getId())) {
                            return ResponseEntity.status(403).<Void>build();
                        }
                    }

                    answerRepository.delete(answer);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= FILTER APIs =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerRepository.findByQuestion_Id(questionId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(answerRepository.findByTeacher_Id(teacherId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/question/{questionId}/teacher/{teacherId}")
    public ResponseEntity<List<Answer>> getByQuestionAndTeacher(
            @PathVariable Long questionId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(answerRepository.findByQuestion_IdAndTeacher_Id(questionId, teacherId));
    }
}
