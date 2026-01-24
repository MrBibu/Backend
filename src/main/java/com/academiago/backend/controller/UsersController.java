package com.academiago.backend.controller;

import com.academiago.backend.model.Role;
import com.academiago.backend.model.Users;
import com.academiago.backend.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersRepository usersRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Users> createUser(
            @Valid @RequestBody Users user
    ) {
        return ResponseEntity.ok(usersRepository.save(user));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody Users updatedUser
    ) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setRole(updatedUser.getRole());
                    user.setEnabled(updatedUser.getEnabled());
                    return ResponseEntity.ok(usersRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE (hard delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!usersRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usersRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By username
    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getByUsername(@PathVariable String username) {
        return usersRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // By email
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getByEmail(@PathVariable String email) {
        return usersRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // By role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Users>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(usersRepository.findByRole(role));
    }

    // Enabled users only
    @GetMapping("/enabled")
    public ResponseEntity<List<Users>> getEnabledUsers() {
        return ResponseEntity.ok(usersRepository.findByEnabledTrue());
    }

    // Enabled users by role
    @GetMapping("/enabled/role/{role}")
    public ResponseEntity<List<Users>> getEnabledUsersByRole(
            @PathVariable Role role
    ) {
        return ResponseEntity.ok(usersRepository.findByRoleAndEnabledTrue(role));
    }
}
