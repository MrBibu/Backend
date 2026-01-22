package com.academiago.backend.repository;

import com.academiago.backend.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    List<Notice> findByFaculty(String faculty);
    List<Notice> findBySemester(String semester);
}
