package com.academiago.backend.repository;

import com.academiago.backend.model.Notice;
import com.academiago.backend.model.Users;
import com.academiago.backend.model.NoticeVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByUploadedBy(Users user);
    List<Notice> findByVisibleTo(NoticeVisibility visibility);
}
