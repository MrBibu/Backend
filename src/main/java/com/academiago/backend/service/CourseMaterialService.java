package com.academiago.backend.service;

import com.academiago.backend.model.CourseMaterial;
import com.academiago.backend.repository.CourseMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseMaterialService {

    private final CourseMaterialRepository courseMaterialRepository;

    public List<CourseMaterial> getAllMaterials() { return courseMaterialRepository.findAll(); }
    public Optional<CourseMaterial> getMaterialById(Integer id) { return courseMaterialRepository.findById(id); }
    public CourseMaterial createMaterial(CourseMaterial material) { return courseMaterialRepository.save(material); }

    public CourseMaterial updateMaterial(Integer id, CourseMaterial updatedMaterial) {
        return courseMaterialRepository.findById(id).map(m -> {
            m.setTitle(updatedMaterial.getTitle());
            m.setMaterialType(updatedMaterial.getMaterialType());
            m.setFaculty(updatedMaterial.getFaculty());
            m.setSemester(updatedMaterial.getSemester());
            m.setFileUrl(updatedMaterial.getFileUrl());
            m.setFileSize(updatedMaterial.getFileSize());
            m.setFileType(updatedMaterial.getFileType());
            m.setDescription(updatedMaterial.getDescription());
            return courseMaterialRepository.save(m);
        }).orElseThrow(() -> new RuntimeException("Material not found"));
    }

    public void deleteMaterial(Integer id) { courseMaterialRepository.deleteById(id); }
}
