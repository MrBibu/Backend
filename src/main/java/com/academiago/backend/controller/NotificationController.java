package com.academiago.backend.controller;

import com.academiago.backend.model.Notification;
import com.academiago.backend.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Notification> createNotification(@Valid @RequestBody Notification notification) {
        return ResponseEntity.ok(notificationRepository.save(notification));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody Notification updated
    ) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setUser(updated.getUser());
                    notification.setMessage(updated.getMessage());
                    notification.setRead(updated.getRead());
                    return ResponseEntity.ok(notificationRepository.save(notification));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (!notificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationRepository.findByUser_Id(userId));
    }

    // By read status
    @GetMapping("/read/{read}")
    public ResponseEntity<List<Notification>> getByReadStatus(@PathVariable Boolean read) {
        return ResponseEntity.ok(notificationRepository.findByRead(read));
    }

    // By user + read status
    @GetMapping("/user/{userId}/read/{read}")
    public ResponseEntity<List<Notification>> getByUserAndRead(
            @PathVariable Long userId,
            @PathVariable Boolean read
    ) {
        return ResponseEntity.ok(notificationRepository.findByUser_IdAndRead(userId, read));
    }
}
