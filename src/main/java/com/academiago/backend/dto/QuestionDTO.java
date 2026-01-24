package com.academiago.backend.dto;

import com.academiago.backend.model.QuestionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    // optional – some questions may not be assigned yet
    private Long teacherId;

    @NotBlank(message = "Question text is required")
    private String text;

    @NotNull(message = "Question status is required")
    private QuestionStatus status;
}
