package com.academiago.backend.service;

import com.academiago.backend.model.Answer;
import com.academiago.backend.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public List<Answer> getAllAnswers() { return answerRepository.findAll(); }
    public Optional<Answer> getAnswerById(Integer id) { return answerRepository.findById(id); }
    public Answer createAnswer(Answer answer) { return answerRepository.save(answer); }

    public Answer updateAnswer(Integer id, Answer updatedAnswer) {
        return answerRepository.findById(id).map(a -> {
            a.setAnswerText(updatedAnswer.getAnswerText());
            a.setTeacher(updatedAnswer.getTeacher());
            a.setQuestion(updatedAnswer.getQuestion());
            a.setIsEdited(updatedAnswer.getIsEdited());
            return answerRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Answer not found"));
    }

    public void deleteAnswer(Integer id) { answerRepository.deleteById(id); }
}
