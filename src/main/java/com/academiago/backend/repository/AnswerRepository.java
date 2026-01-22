package com.academiago.backend.repository;

import com.academiago.backend.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Answer findByQuestionId(Integer questionId);
}
