package com.academiago.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseMaterialDTO {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotBlank(message = "File URL is required")
    private String fileURL;
}
