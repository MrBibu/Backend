package com.academiago.backend.dto;

import com.academiago.backend.model.NoticeVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Visibility is required")
    private NoticeVisibility visibleTo;

    // Optional filters (can be null depending on visibility)
    private Long facultyId;
    private Long programId;
    private Long semesterId;
}
