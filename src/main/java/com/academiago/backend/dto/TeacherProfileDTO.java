package com.academiago.backend.dto;

import com.academiago.backend.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherProfileDTO {

    @NotNull(message = "User ID is required")
    private Long userId; // link to Users entity

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String qualifications;
    private String permanentAddress;
    private String temporaryAddress;

    @NotNull(message = "Date of Birth is required")
    private Date dob;

    private String contactNo;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private Boolean status = true; // default active
}
