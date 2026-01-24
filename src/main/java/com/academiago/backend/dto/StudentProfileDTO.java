package com.academiago.backend.dto;

import com.academiago.backend.model.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Permanent address is required")
    private String permanentAddress;

    @NotNull(message = "Temporary address is required")
    private String temporaryAddress;

    private Long rollNumber;

    @NotNull(message = "Batch year is required")
    private String batchYear;

    @NotNull(message = "Program ID is required")
    private Long programId;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;
}
