package com.academiago.backend.controller;

import com.academiago.backend.dto.UsersDTO;
import com.academiago.backend.model.Role;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersRepository usersRepository;

    // CREATE (only ADMIN can create users)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Users> createUser(@Valid @RequestBody UsersDTO dto) {
        Users user = Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword()) // remember to encode with BCrypt in service layer
                .role(dto.getRole())
                .enabled(dto.getEnabled())
                .build();

        return ResponseEntity.ok(usersRepository.save(user));
    }

    // READ ALL (only ADMIN can view all users)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    // READ BY ID (ADMIN can view any user, TEACHER/STUDENT can only view their own profile)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (only ADMIN can update users)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UsersDTO dto
    ) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setUsername(dto.getUsername());
                    user.setEmail(dto.getEmail());
                    user.setPassword(dto.getPassword()); // encode in service
                    user.setRole(dto.getRole());
                    user.setEnabled(dto.getEnabled());
                    return ResponseEntity.ok(usersRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE (only ADMIN can delete users)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!usersRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usersRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // FILTERS (ADMIN can query by username/email/role, STUDENT/TEACHER can only query themselves)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getByUsername(@PathVariable String username) {
        return usersRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getByEmail(@PathVariable String email) {
        return usersRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Users>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(usersRepository.findByRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enabled")
    public ResponseEntity<List<Users>> getEnabledUsers() {
        return ResponseEntity.ok(usersRepository.findByEnabledTrue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enabled/role/{role}")
    public ResponseEntity<List<Users>> getEnabledUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(usersRepository.findByRoleAndEnabledTrue(role));
    }
}
