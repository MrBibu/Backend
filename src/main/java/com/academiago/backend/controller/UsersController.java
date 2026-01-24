package com.academiago.backend.controller;

import com.academiago.backend.dto.UsersDTO;
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

    // CREATE
    @PostMapping
    public ResponseEntity<Users> createUser(@Valid @RequestBody UsersDTO dto) {
        Users user = Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .enabled(dto.getEnabled())
                .build();

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
            @Valid @RequestBody UsersDTO dto
    ) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setUsername(dto.getUsername());
                    user.setEmail(dto.getEmail());
                    user.setPassword(dto.getPassword());
                    user.setRole(dto.getRole());
                    user.setEnabled(dto.getEnabled());
                    return ResponseEntity.ok(usersRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!usersRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usersRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // FILTERS
    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getByUsername(@PathVariable String username) {
        return usersRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getByEmail(@PathVariable String email) {
        return usersRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Users>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(usersRepository.findByRole(role));
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<Users>> getEnabledUsers() {
        return ResponseEntity.ok(usersRepository.findByEnabledTrue());
    }

    @GetMapping("/enabled/role/{role}")
    public ResponseEntity<List<Users>> getEnabledUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(usersRepository.findByRoleAndEnabledTrue(role));
    }
}
