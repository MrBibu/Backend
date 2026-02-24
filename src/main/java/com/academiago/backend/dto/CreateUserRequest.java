package com.academiago.backend.dto;

import com.academiago.backend.model.Role;
import lombok.Data;
import java.io.Serializable;

// nested DTOs used when the admin provides profile data directly
import com.academiago.backend.dto.StudentProfileDTO;
import com.academiago.backend.dto.TeacherProfileDTO;

// request used when an admin registers/creates a new user. optional student/teacher
// profile sections are included so the UI can send additional info in a single
// call if desired; otherwise the client may hit the profile endpoints separately.
@Data
public class CreateUserRequest {

    private String username;
    private String email;
    private Role role;
    private String tempPassword;

    // if the role is STUDENT the client may supply a nested profile object with
    // the required fields. the controller will ignore it for ADMIN.
    private StudentProfileDTO studentProfile;

    // similarly for teacher
    private TeacherProfileDTO teacherProfile;
}

