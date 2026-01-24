package com.academiago.backend.controller;

import com.academiago.backend.dto.NotificationDTO;
import com.academiago.backend.model.Notification;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.NotificationRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @Valid @RequestBody NotificationDTO dto
    ) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .message(dto.getMessage())
                .read(dto.getRead())
                .build();

        return ResponseEntity.ok(notificationRepository.save(notification));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationDTO dto
    ) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setUser(
                            usersRepository.findById(dto.getUserId())
                                    .orElseThrow(() -> new RuntimeException("User not found"))
                    );
                    notification.setMessage(dto.getMessage());
                    notification.setRead(dto.getRead());

                    return ResponseEntity.ok(notificationRepository.save(notification));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (!notificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationRepository.findByUser_Id(userId)
        );
    }

    @GetMapping("/read/{read}")
    public ResponseEntity<List<Notification>> getByReadStatus(@PathVariable Boolean read) {
        return ResponseEntity.ok(
                notificationRepository.findByRead(read)
        );
    }

    @GetMapping("/user/{userId}/read/{read}")
    public ResponseEntity<List<Notification>> getByUserAndRead(
            @PathVariable Long userId,
            @PathVariable Boolean read
    ) {
        return ResponseEntity.ok(
                notificationRepository.findByUser_IdAndRead(userId, read)
        );
    }
}
