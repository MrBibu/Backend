package com.academiago.backend.repository;

import com.academiago.backend.model.Assignment;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findBySubject(Subject subject);
    List<Assignment> findByTeacher(TeacherProfile teacher);
}
