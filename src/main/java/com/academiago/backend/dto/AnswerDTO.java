package com.academiago.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDTO {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Text is required")
    private String text;
}
