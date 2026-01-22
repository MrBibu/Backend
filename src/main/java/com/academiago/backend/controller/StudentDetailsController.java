package com.academiago.backend.controller;

import com.academiago.backend.model.StudentDetails;
import com.academiago.backend.service.StudentDetailsService;
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
    public ResponseEntity<List<StudentDetails>> getAllStudents() { return ResponseEntity.ok(studentDetailsService.getAllStudents()); }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetails> getStudentById(@PathVariable Integer id) {
        return studentDetailsService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDetails> createStudent(@RequestBody StudentDetails student) { return ResponseEntity.ok(studentDetailsService.createStudent(student)); }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetails> updateStudent(@PathVariable Integer id, @RequestBody StudentDetails student) {
        return ResponseEntity.ok(studentDetailsService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentDetailsService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
