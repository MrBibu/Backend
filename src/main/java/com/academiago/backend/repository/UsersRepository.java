package com.academiago.backend.repository;

import com.academiago.backend.model.UserRole;
import com.academiago.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findByRole(UserRole role);

    List<Users> findByRoleIn(List<UserRole> roles);

    List<Users> findByActiveTrue();

    List<Users> findByRoleAndActiveTrue(UserRole role);
}
