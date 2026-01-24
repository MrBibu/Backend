package com.academiago.backend.repository;

import com.academiago.backend.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion_Id(Long questionId);

    List<Answer> findByTeacher_Id(Long teacherId);

    List<Answer> findByQuestion_IdAndTeacher_Id(Long questionId, Long teacherId);
}
