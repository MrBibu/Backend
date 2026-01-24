package com.academiago.backend.repository;

import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

    Optional<TeacherProfile> findByUser(Users user);

    Optional<TeacherProfile> findByUser_Id(Long userId);

    List<TeacherProfile> findByStatusTrue();

    List<TeacherProfile> findByGender(Enum gender);
}
