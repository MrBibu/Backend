package com.academiago.backend.repository;

import com.academiago.backend.model.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Integer> {

    Optional<StudentDetails> findByEnrollmentId(String enrollmentId);

    Optional<StudentDetails> findByPhone(String phone);

    Optional<StudentDetails> findByGuardianPhone(String guardianPhone);

    Optional<StudentDetails> findByUser_Id(Integer userId);
}
