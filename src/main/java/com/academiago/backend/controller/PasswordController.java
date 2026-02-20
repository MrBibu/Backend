package com.academiago.backend.controller;

import com.academiago.backend.dto.ChangePasswordRequest;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PasswordController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        Users user = usersRepository.findByUsername(principal.getName()).get();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        user.setFirstLogin(false);
        user.setTempPassword(false);

        usersRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }
}
