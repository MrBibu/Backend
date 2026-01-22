package com.academiago.backend.service;

import com.academiago.backend.model.Users;
import com.academiago.backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public List<Users> getAllUsers() { return usersRepository.findAll(); }
    public Optional<Users> getUserById(Integer id) { return usersRepository.findById(id); }
    public Users createUser(Users user) { return usersRepository.save(user); }

    public Users updateUser(Integer id, Users updatedUser) {
        return usersRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setFaculty(updatedUser.getFaculty());
            user.setSemester(updatedUser.getSemester());
            user.setRole(updatedUser.getRole());
            user.setStatus(updatedUser.getStatus());
            return usersRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Integer id) { usersRepository.deleteById(id); }
}
