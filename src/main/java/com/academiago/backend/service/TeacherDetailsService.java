package com.academiago.backend.service;

import com.academiago.backend.model.TeacherDetails;
import com.academiago.backend.repository.TeacherDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherDetailsService {
    private final TeacherDetailsRepository teacherDetailsRepository;

    public List<TeacherDetails> getAllTeachers() { return teacherDetailsRepository.findAll(); }
    public Optional<TeacherDetails> getTeacherById(Integer id) { return teacherDetailsRepository.findById(id); }
    public TeacherDetails createTeacher(TeacherDetails teacher) { return teacherDetailsRepository.save(teacher); }

    public TeacherDetails updateTeacher(Integer id, TeacherDetails updatedTeacher) {
        return teacherDetailsRepository.findById(id).map(teacher -> {
            teacher.setPhone(updatedTeacher.getPhone());
            teacher.setSpecialization(updatedTeacher.getSpecialization());
            teacher.setHireDate(updatedTeacher.getHireDate());
            return teacherDetailsRepository.save(teacher);
        }).orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    public void deleteTeacher(Integer id) { teacherDetailsRepository.deleteById(id); }
}
