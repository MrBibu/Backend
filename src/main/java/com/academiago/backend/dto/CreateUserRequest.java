package com.academiago.backend.dto;

import com.academiago.backend.model.Role;
import lombok.Data;

@Data
public class CreateUserRequest {

    private String username;
    private String email;
    private Role role;
    private String tempPassword;
}

