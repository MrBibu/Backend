package com.academiago.backend.service;

import com.academiago.backend.model.Assignment;
import com.academiago.backend.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public List<Assignment> getAllAssignments() { return assignmentRepository.findAll(); }
    public Optional<Assignment> getAssignmentById(Integer id) { return assignmentRepository.findById(id); }
    public Assignment createAssignment(Assignment assignment) { return assignmentRepository.save(assignment); }

    public Assignment updateAssignment(Integer id, Assignment updatedAssignment) {
        return assignmentRepository.findById(id).map(a -> {
            a.setTitle(updatedAssignment.getTitle());
            a.setDescription(updatedAssignment.getDescription());
            a.setDueDate(updatedAssignment.getDueDate());
            a.setFaculty(updatedAssignment.getFaculty());
            a.setSemester(updatedAssignment.getSemester());
            a.setAttachmentUrl(updatedAssignment.getAttachmentUrl());
            a.setAttachmentType(updatedAssignment.getAttachmentType());
            a.setAttachmentSize(updatedAssignment.getAttachmentSize());
            a.setActive(updatedAssignment.isActive());
            return assignmentRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public void deleteAssignment(Integer id) { assignmentRepository.deleteById(id); }
}
