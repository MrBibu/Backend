package com.academiago.backend.dto;

public record LoginResponse(
        String token,
        String role,
        Boolean firstLogin,
        Long userId
) {}