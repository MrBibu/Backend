package com.academiago.backend.controller;

import com.academiago.backend.dto.ChangePasswordRequest;
import com.academiago.backend.dto.CreateUserRequest;
import com.academiago.backend.dto.LoginRequest;
import com.academiago.backend.dto.LoginResponse;
import com.academiago.backend.model.Role;
import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.StudentProfileRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
import com.academiago.backend.repository.UsersRepository;
import com.academiago.backend.security.CustomUserDetails;
import com.academiago.backend.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsersRepository usersRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // ============================
    // ADMIN REGISTRATION
    // ============================

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody CreateUserRequest request) {

        if (usersRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        Users admin = new Users();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getTempPassword()));
        admin.setRole(Role.ADMIN);
        admin.setFirstLogin(true);
        admin.setTempPassword(true);

        usersRepository.save(admin);

        return ResponseEntity.ok("Admin registered successfully");
    }

    // ============================
    // ADMIN CREATES USER
    // ============================

    @Transactional
    @PostMapping("/admin/create-user")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {

        if (usersRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getTempPassword()));
        user.setRole(request.getRole());
        user.setFirstLogin(true);
        user.setTempPassword(true);

        Users savedUser = usersRepository.save(user);

        if (request.getRole() == Role.STUDENT) {
            StudentProfile profile = new StudentProfile();
            profile.setUser(savedUser);
            studentProfileRepository.save(profile);
        }
        else if (request.getRole() == Role.TEACHER) {
            TeacherProfile profile = new TeacherProfile();
            profile.setUser(savedUser);
            teacherProfileRepository.save(profile);
        }

        return ResponseEntity.ok("User created with temporary password");
    }

    // ============================
    // LOGIN
    // ============================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Users user = userDetails.user();

        // Generate token (NO STATIC CALL)
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        // Return firstLogin flag instead of blocking login
        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        user.getRole().name(),
                        user.getFirstLogin()
                )
        );
    }

    // ============================
    // CHANGE PASSWORD
    // ============================

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        user.setTempPassword(false);

        usersRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

    // ============================
    // VALIDATE TOKEN
    // ============================

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(
            @RequestParam String token
    ) {

        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok("Token is valid");
        }

        return ResponseEntity.status(401).body("Invalid token");
    }
}