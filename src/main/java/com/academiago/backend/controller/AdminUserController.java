package com.academiago.backend.controller;

import com.academiago.backend.dto.CreateUserRequest;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // temporary password
        String tempPass = request.getTempPassword();
        user.setPassword(passwordEncoder.encode(tempPass));

        user.setFirstLogin(true);
        user.setTempPassword(true);

        usersRepository.save(user);

        return ResponseEntity.ok("User created with temporary password");
    }
}

