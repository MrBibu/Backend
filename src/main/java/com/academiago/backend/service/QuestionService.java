package com.academiago.backend.service;

import com.academiago.backend.model.Question;
import com.academiago.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions() { return questionRepository.findAll(); }
    public Optional<Question> getQuestionById(Integer id) { return questionRepository.findById(id); }
    public Question createQuestion(Question question) { return questionRepository.save(question); }

    public Question updateQuestion(Integer id, Question updatedQuestion) {
        return questionRepository.findById(id).map(q -> {
            q.setQuestionText(updatedQuestion.getQuestionText());
            q.setTeacher(updatedQuestion.getTeacher());
            q.setAssignment(updatedQuestion.getAssignment());
            q.setAnswered(updatedQuestion.getAnswered());
            return questionRepository.save(q);
        }).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public void deleteQuestion(Integer id) { questionRepository.deleteById(id); }
}
