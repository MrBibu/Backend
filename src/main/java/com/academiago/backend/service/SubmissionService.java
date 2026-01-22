package com.academiago.backend.service;

import com.academiago.backend.model.Submission;
import com.academiago.backend.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public List<Submission> getAllSubmissions() { return submissionRepository.findAll(); }
    public Optional<Submission> getSubmissionById(Integer id) { return submissionRepository.findById(id); }
    public Submission createSubmission(Submission submission) { return submissionRepository.save(submission); }

    public Submission updateSubmission(Integer id, Submission updatedSubmission) {
        return submissionRepository.findById(id).map(s -> {
            s.setAssignment(updatedSubmission.getAssignment());
            s.setStudent(updatedSubmission.getStudent());
            s.setSubmission(updatedSubmission.getSubmission());
            s.setGrades(updatedSubmission.getGrades());
            s.setFeedback(updatedSubmission.getFeedback());
            s.setStatus(updatedSubmission.getStatus());
            return submissionRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    public void deleteSubmission(Integer id) { submissionRepository.deleteById(id); }
}
