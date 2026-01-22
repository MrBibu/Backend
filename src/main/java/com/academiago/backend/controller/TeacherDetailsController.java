package com.academiago.backend.controller;

import com.academiago.backend.model.TeacherDetails;
import com.academiago.backend.service.TeacherDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherDetailsController {
    private final TeacherDetailsService teacherDetailsService;

    @GetMapping
    public ResponseEntity<List<TeacherDetails>> getAllTeachers() { return ResponseEntity.ok(teacherDetailsService.getAllTeachers()); }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDetails> getTeacherById(@PathVariable Integer id) {
        return teacherDetailsService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeacherDetails> createTeacher(@RequestBody TeacherDetails teacher) { return ResponseEntity.ok(teacherDetailsService.createTeacher(teacher)); }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDetails> updateTeacher(@PathVariable Integer id, @RequestBody TeacherDetails teacher) {
        return ResponseEntity.ok(teacherDetailsService.updateTeacher(id, teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        teacherDetailsService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
