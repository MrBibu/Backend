package com.academiago.backend.repository;

import com.academiago.backend.model.Faculty;
import com.academiago.backend.model.Notice;
import com.academiago.backend.model.NoticeVisibility;
import com.academiago.backend.model.Program;
import com.academiago.backend.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByVisibleTo(NoticeVisibility visibleTo);

    List<Notice> findByFaculty_Id(Long facultyId);

    List<Notice> findByProgram_Id(Long programId);

    List<Notice> findBySemester_Id(Long semesterId);

    List<Notice> findByVisibleToAndFaculty_Id(NoticeVisibility visibleTo, Long facultyId);

    List<Notice> findByVisibleToAndProgram_Id(NoticeVisibility visibleTo, Long programId);

    List<Notice> findByVisibleToAndSemester_Id(NoticeVisibility visibleTo, Long semesterId);
}
