package com.academiago.backend.controller;

import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.repository.service.CourseMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;

    @GetMapping
    public ResponseEntity<List<CourseMaterial>> getAllMaterials() { return ResponseEntity.ok(courseMaterialService.getAllMaterials()); }

    @GetMapping("/{id}")
    public ResponseEntity<CourseMaterial> getMaterialById(@PathVariable Integer id) {
        return courseMaterialService.getMaterialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseMaterial> createMaterial(@RequestBody CourseMaterial material) { return ResponseEntity.ok(courseMaterialService.createMaterial(material)); }

    @PutMapping("/{id}")
    public ResponseEntity<CourseMaterial> updateMaterial(@PathVariable Integer id, @RequestBody CourseMaterial material) {
        return ResponseEntity.ok(courseMaterialService.updateMaterial(id, material));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id) {
        courseMaterialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
