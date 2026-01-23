package com.academiago.backend.repository;

import com.academiago.backend.model.Subject;
import com.academiago.backend.model.Semester;
import com.academiago.backend.model.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySemester(Semester semester);
    List<Subject> findByTeacher(TeacherProfile teacher);
}
