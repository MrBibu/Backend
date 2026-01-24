package com.academiago.backend.controller;

import com.academiago.backend.dto.NotificationDTO;
import com.academiago.backend.model.Notification;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.NotificationRepository;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    // ================= CREATE =================
    // Only ADMIN can create notifications for any user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Notification> createNotification(@Valid @RequestBody NotificationDTO dto) {
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
    // Admin can view all, users can only view their own
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(notificationRepository.findAll());
        }

        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        return ResponseEntity.ok(notificationRepository.findByUser_Id(loggedInUser.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id, Authentication authentication) {
        return (ResponseEntity<Notification>) notificationRepository.findById(id)
                .map(notification -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (!role.equals("ROLE_ADMIN")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

                        if (!notification.getUser().getId().equals(loggedInUser.getId())) {
                            return ResponseEntity.status(403).build();
                        }
                    }
                    return ResponseEntity.ok(notification);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    // Admin can update any, users can only update their own
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationDTO dto,
            Authentication authentication
    ) {
        return (ResponseEntity<Notification>) notificationRepository.findById(id)
                .map(notification -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (!role.equals("ROLE_ADMIN")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

                        if (!notification.getUser().getId().equals(loggedInUser.getId())) {
                            return ResponseEntity.status(403).build();
                        }
                    }

                    notification.setMessage(dto.getMessage());
                    notification.setRead(dto.getRead());
                    return ResponseEntity.ok(notificationRepository.save(notification));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    // Admin can delete any, users can only delete their own
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, Authentication authentication) {
        return (ResponseEntity<Void>) notificationRepository.findById(id)
                .map(notification -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    if (!role.equals("ROLE_ADMIN")) {
                        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

                        if (!notification.getUser().getId().equals(loggedInUser.getId())) {
                            return ResponseEntity.status(403).<Void>build();
                        }
                    }

                    notificationRepository.delete(notification);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= FILTER APIs =================
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (!role.equals("ROLE_ADMIN")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            if (!loggedInUser.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(notificationRepository.findByUser_Id(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/read/{read}")
    public ResponseEntity<List<Notification>> getByReadStatus(@PathVariable Boolean read, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(notificationRepository.findByRead(read));
        }

        Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        return ResponseEntity.ok(notificationRepository.findByUser_IdAndRead(loggedInUser.getId(), read));
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/user/{userId}/read/{read}")
    public ResponseEntity<List<Notification>> getByUserAndRead(
            @PathVariable Long userId,
            @PathVariable Boolean read,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (!role.equals("ROLE_ADMIN")) {
            Users loggedInUser = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            if (!loggedInUser.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(notificationRepository.findByUser_IdAndRead(userId, read));
    }
}
