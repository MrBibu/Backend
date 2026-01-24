package com.academiago.backend.controller;

import com.academiago.backend.model.Program;
import com.academiago.backend.repository.ProgramRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramRepository programRepository;

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Program> createProgram(@Valid @RequestBody Program program) {
        return ResponseEntity.ok(programRepository.save(program));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {
        return ResponseEntity.ok(programRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable Long id) {
        return programRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(@PathVariable Long id, @Valid @RequestBody Program updated) {
        return programRepository.findById(id)
                .map(program -> {
                    program.setName(updated.getName());
                    program.setFaculty(updated.getFaculty());
                    return ResponseEntity.ok(programRepository.save(program));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        if (!programRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        programRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Program>> getProgramsByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(programRepository.findByFaculty_Id(facultyId));
    }

    // By name + faculty
    @GetMapping("/faculty/{facultyId}/name/{name}")
    public ResponseEntity<Program> getProgramByNameAndFaculty(
            @PathVariable Long facultyId,
            @PathVariable String name
    ) {
        return programRepository.findByNameAndFaculty_Id(name, facultyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
