package com.academiago.backend.repository;

import com.academiago.backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByAssignmentId(Integer assignmentId);
    List<Question> findByTeacherId(Integer teacherId);
}
