package com.academiago.backend.controller;

import com.academiago.backend.model.TeacherProfile;
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
    public ResponseEntity<List<TeacherProfile>> getAllTeachers() { return ResponseEntity.ok(teacherDetailsService.getAllTeachers()); }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherProfile> getTeacherById(@PathVariable Integer id) {
        return teacherDetailsService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeacherProfile> createTeacher(@RequestBody TeacherProfile teacher) { return ResponseEntity.ok(teacherDetailsService.createTeacher(teacher)); }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherProfile> updateTeacher(@PathVariable Integer id, @RequestBody TeacherProfile teacher) {
        return ResponseEntity.ok(teacherDetailsService.updateTeacher(id, teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        teacherDetailsService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
