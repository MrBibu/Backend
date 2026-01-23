package com.academiago.backend.repository;

import com.academiago.backend.model.Users;
import com.academiago.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    List<Users> findByRole(Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
