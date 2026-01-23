package com.academiago.backend.repository;

import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    List<TeacherProfile> findByFaculty(Faculty faculty);
}
