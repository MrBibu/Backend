package com.academiago.backend.repository;

import com.academiago.backend.model.TeacherDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherDetailsRepository extends JpaRepository<TeacherDetails, Integer> {

    Optional<TeacherDetails> findByUser_Id(Integer userId);

    Optional<TeacherDetails> findByPhone(String phone);
}
