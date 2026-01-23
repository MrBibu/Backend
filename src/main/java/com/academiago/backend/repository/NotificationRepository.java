package com.academiago.backend.repository;

import com.academiago.backend.model.Notification;
import com.academiago.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(Users user);
    List<Notification> findByUserAndReadFalse(Users user);
}
