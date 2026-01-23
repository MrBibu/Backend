package com.academiago.backend.controller;

import com.academiago.backend.model.StudentProfile;
import com.academiago.backend.repository.service.StudentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentDetailsController {
    private final StudentDetailsService studentDetailsService;

    @GetMapping
    public ResponseEntity<List<StudentProfile>> getAllStudents() { return ResponseEntity.ok(studentDetailsService.getAllStudents()); }

    @GetMapping("/{id}")
    public ResponseEntity<StudentProfile> getStudentById(@PathVariable Integer id) {
        return studentDetailsService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentProfile> createStudent(@RequestBody StudentProfile student) { return ResponseEntity.ok(studentDetailsService.createStudent(student)); }

    @PutMapping("/{id}")
    public ResponseEntity<StudentProfile> updateStudent(@PathVariable Integer id, @RequestBody StudentProfile student) {
        return ResponseEntity.ok(studentDetailsService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentDetailsService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
