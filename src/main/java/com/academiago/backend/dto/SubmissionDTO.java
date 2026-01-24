package com.academiago.backend.dto;

import com.academiago.backend.model.SubmissionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionDTO {

    // Used for response only
    private Long id;
    private LocalDateTime submittedAt;

    // Used for create request
    @NotNull
    private Long assignmentId;

    @NotNull
    private Long studentId;

    // Used for update request
    private SubmissionStatus status;
}
