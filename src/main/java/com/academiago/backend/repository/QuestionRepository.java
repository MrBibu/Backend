package com.academiago.backend.repository;

import com.academiago.backend.model.Question;
import com.academiago.backend.model.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByStudent_Id(Long studentId);

    List<Question> findBySubject_Id(Long subjectId);

    List<Question> findByTeacher_Id(Long teacherId);

    List<Question> findByStatus(QuestionStatus status);

    List<Question> findBySubject_IdAndStatus(Long subjectId, QuestionStatus status);

    List<Question> findByStudent_IdAndStatus(Long studentId, QuestionStatus status);
}
