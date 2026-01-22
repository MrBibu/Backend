package com.academiago.backend.controller;

import com.academiago.backend.model.Semester;
import com.academiago.backend.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    @Autowired
    private SemesterRepository semesterRepository;


    @GetMapping
    public List<Semester> getALlSemesters() {
        return semesterRepository.findAll();
    }
    @PostMapping
    public Semester createSemester(@RequestBody Semester semester) {
        return semesterRepository.save(semester);
    }
}
