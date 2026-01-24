package com.academiago.backend.repository;

import com.academiago.backend.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Fetch all answers for a given question
    List<Answer> findByQuestion_Id(Long questionId);

    // Fetch all answers written by a specific teacher
    List<Answer> findByTeacher_Id(Long teacherId);

    // Fetch all answers for a given question by a specific teacher
    List<Answer> findByQuestion_IdAndTeacher_Id(Long questionId, Long teacherId);
}
