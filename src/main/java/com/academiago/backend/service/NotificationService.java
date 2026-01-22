package com.academiago.backend.service;

import com.academiago.backend.model.Notification;
import com.academiago.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() { return notificationRepository.findAll(); }
    public Optional<Notification> getNotificationById(Integer id) { return notificationRepository.findById(id); }
    public Notification createNotification(Notification notification) { return notificationRepository.save(notification); }

    public Notification updateNotification(Integer id, Notification updatedNotification) {
        return notificationRepository.findById(id).map(n -> {
            n.setType(updatedNotification.getType());
            n.setMessage(updatedNotification.getMessage());
            n.setPayload(updatedNotification.getPayload());
            n.setIsRead(updatedNotification.getIsRead());
            n.setUser(updatedNotification.getUser());
            return notificationRepository.save(n);
        }).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public void deleteNotification(Integer id) { notificationRepository.deleteById(id); }
}
