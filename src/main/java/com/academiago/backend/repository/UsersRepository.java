package com.academiago.backend.repository;

import com.academiago.backend.model.Role;
import com.academiago.backend.model.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findByRole(Role role);

    List<Users> findByEnabledTrue();

    List<Users> findByRoleAndEnabledTrue(Role role);

    boolean existsByUsername(@NotBlank String username);
}
