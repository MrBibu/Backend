package com.academiago.backend.repository;

import com.academiago.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_Id(Long userId);

    List<Notification> findByRead(Boolean read);

    List<Notification> findByUser_IdAndRead(Long userId, Boolean read);
}
