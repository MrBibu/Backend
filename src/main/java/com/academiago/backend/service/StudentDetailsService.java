package com.academiago.backend.service;

import com.academiago.backend.model.StudentDetails;
import com.academiago.backend.repository.StudentDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentDetailsService {
    private final StudentDetailsRepository studentDetailsRepository;

    public List<StudentDetails> getAllStudents() { return studentDetailsRepository.findAll(); }
    public Optional<StudentDetails> getStudentById(Integer id) { return studentDetailsRepository.findById(id); }
    public StudentDetails createStudent(StudentDetails student) { return studentDetailsRepository.save(student); }

    public StudentDetails updateStudent(Integer id, StudentDetails updatedStudent) {
        return studentDetailsRepository.findById(id).map(student -> {
            student.setEnrollmentId(updatedStudent.getEnrollmentId());
            student.setPhone(updatedStudent.getPhone());
            student.setGuardianName(updatedStudent.getGuardianName());
            student.setGuardianPhone(updatedStudent.getGuardianPhone());
            return studentDetailsRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public void deleteStudent(Integer id) { studentDetailsRepository.deleteById(id); }
}
