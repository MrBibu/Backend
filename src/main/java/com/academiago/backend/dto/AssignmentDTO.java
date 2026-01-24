package com.academiago.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherProfileId;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
