package com.academiago.backend.controller;

import com.academiago.backend.dto.LoginRequest;
import com.academiago.backend.dto.LoginResponse;
import com.academiago.backend.model.Users;
import com.academiago.backend.model.Role;
import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.model.TeacherProfile;
import com.academiago.backend.repository.UsersRepository;
import com.academiago.backend.repository.StudentProfileRepository;
import com.academiago.backend.repository.TeacherProfileRepository;
import com.academiago.backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        usersRepository.save(user);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@Valid @RequestBody Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);
        Users savedUser = usersRepository.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setUser(savedUser);
        studentProfileRepository.save(profile);

        return ResponseEntity.ok("Student registered successfully");
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(@Valid @RequestBody Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.TEACHER);
        Users savedUser = usersRepository.save(user);

        TeacherProfile profile = new TeacherProfile();
        profile.setUser(savedUser);
        teacherProfileRepository.save(profile);

        return ResponseEntity.ok("Teacher registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Users user = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name()));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok("Token is valid");
        }
        return ResponseEntity.status(401).body("Invalid token");
    }
}
