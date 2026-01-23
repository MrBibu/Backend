package com.academiago.backend.repository;

import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.model.Subject;
import com.academiago.backend.model.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findBySubject(Subject subject);
    List<CourseMaterial> findByUploadedBy(TeacherProfile teacher);
}
