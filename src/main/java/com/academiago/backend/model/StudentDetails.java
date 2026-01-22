package com.academiago.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDetails {

    @Id
    private Integer id; // shared PK with Users

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "enrollment_id", unique = true, nullable = false, length = 50)
    private String enrollmentId;

    private String phone;
    private String guardianName;
    private String guardianPhone;
}
