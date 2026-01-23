package com.academiago.backend.repository;

import com.academiago.backend.model.Question;
import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByStudent(StudentProfile student);
    List<Question> findByTeacher(TeacherProfile teacher);
    List<Question> findBySubject(Subject subject);
}
