package com.academiago.backend.controller;

import com.academiago.backend.dto.NoticeDTO;
import com.academiago.backend.model.*;
import com.academiago.backend.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;
    private final FacultyRepository facultyRepository;
    private final ProgramRepository programRepository;
    private final SemesterRepository semesterRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @Valid @RequestBody NoticeDTO dto
    ) {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .visibleTo(dto.getVisibleTo())
                .faculty(dto.getFacultyId() == null ? null :
                        facultyRepository.findById(dto.getFacultyId())
                                .orElseThrow(() -> new RuntimeException("Faculty not found")))
                .program(dto.getProgramId() == null ? null :
                        programRepository.findById(dto.getProgramId())
                                .orElseThrow(() -> new RuntimeException("Program not found")))
                .semester(dto.getSemesterId() == null ? null :
                        semesterRepository.findById(dto.getSemesterId())
                                .orElseThrow(() -> new RuntimeException("Semester not found")))
                .build();

        return ResponseEntity.ok(noticeRepository.save(notice));
    }

    // ================= READ =================
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        return noticeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable Long id,
            @Valid @RequestBody NoticeDTO dto
    ) {
        return noticeRepository.findById(id)
                .map(notice -> {
                    notice.setTitle(dto.getTitle());
                    notice.setVisibleTo(dto.getVisibleTo());

                    notice.setFaculty(dto.getFacultyId() == null ? null :
                            facultyRepository.findById(dto.getFacultyId())
                                    .orElseThrow(() -> new RuntimeException("Faculty not found")));

                    notice.setProgram(dto.getProgramId() == null ? null :
                            programRepository.findById(dto.getProgramId())
                                    .orElseThrow(() -> new RuntimeException("Program not found")));

                    notice.setSemester(dto.getSemesterId() == null ? null :
                            semesterRepository.findById(dto.getSemesterId())
                                    .orElseThrow(() -> new RuntimeException("Semester not found")));

                    return ResponseEntity.ok(noticeRepository.save(notice));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (!noticeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noticeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs (UNCHANGED) =================

    @GetMapping("/visibility/{visibleTo}")
    public ResponseEntity<List<Notice>> getByVisibility(
            @PathVariable NoticeVisibility visibleTo
    ) {
        return ResponseEntity.ok(
                noticeRepository.findByVisibleTo(visibleTo)
        );
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Notice>> getByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(
                noticeRepository.findByFaculty_Id(facultyId)
        );
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Notice>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(
                noticeRepository.findByProgram_Id(programId)
        );
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<Notice>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(
                noticeRepository.findBySemester_Id(semesterId)
        );
    }

    @GetMapping("/visibility/{visibleTo}/faculty/{facultyId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndFaculty(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long facultyId
    ) {
        return ResponseEntity.ok(
                noticeRepository.findByVisibleToAndFaculty_Id(visibleTo, facultyId)
        );
    }

    @GetMapping("/visibility/{visibleTo}/program/{programId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndProgram(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long programId
    ) {
        return ResponseEntity.ok(
                noticeRepository.findByVisibleToAndProgram_Id(visibleTo, programId)
        );
    }

    @GetMapping("/visibility/{visibleTo}/semester/{semesterId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndSemester(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(
                noticeRepository.findByVisibleToAndSemester_Id(visibleTo, semesterId)
        );
    }
}
