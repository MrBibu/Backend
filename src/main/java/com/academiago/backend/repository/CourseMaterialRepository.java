package com.academiago.backend.repository;

import com.academiago.backend.model.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {

    List<CourseMaterial> findBySubject_Id(Long subjectId);

    List<CourseMaterial> findByUploadedBy_Id(Long teacherId);

    List<CourseMaterial> findBySubject_IdAndUploadedBy_Id(Long subjectId, Long teacherId);
}
