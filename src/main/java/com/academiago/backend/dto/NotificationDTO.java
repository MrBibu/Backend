package com.academiago.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Read status is required")
    private Boolean read;
}
