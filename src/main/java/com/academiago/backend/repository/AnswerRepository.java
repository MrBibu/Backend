package com.academiago.backend.repository;

import com.academiago.backend.model.Answer;
import com.academiago.backend.model.Question;
import com.academiago.backend.model.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);
    List<Answer> findByTeacher(TeacherProfile teacher);
}
