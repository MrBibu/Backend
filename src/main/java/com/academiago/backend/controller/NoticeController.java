package com.academiago.backend.controller;

import com.academiago.backend.model.Notice;
import com.academiago.backend.model.NoticeVisibility;
import com.academiago.backend.repository.NoticeRepository;
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

    // ================= CRUD =================

    // CREATE
    @PostMapping
    public ResponseEntity<Notice> createNotice(@Valid @RequestBody Notice notice) {
        return ResponseEntity.ok(noticeRepository.save(notice));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        return noticeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable Long id,
            @Valid @RequestBody Notice updated
    ) {
        return noticeRepository.findById(id)
                .map(notice -> {
                    notice.setTitle(updated.getTitle());
                    notice.setVisibleTo(updated.getVisibleTo());
                    notice.setFaculty(updated.getFaculty());
                    notice.setProgram(updated.getProgram());
                    notice.setSemester(updated.getSemester());
                    return ResponseEntity.ok(noticeRepository.save(notice));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (!noticeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noticeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FILTER APIs =================

    // By visibility
    @GetMapping("/visibility/{visibleTo}")
    public ResponseEntity<List<Notice>> getByVisibility(@PathVariable NoticeVisibility visibleTo) {
        return ResponseEntity.ok(noticeRepository.findByVisibleTo(visibleTo));
    }

    // By faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Notice>> getByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(noticeRepository.findByFaculty_Id(facultyId));
    }

    // By program
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Notice>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(noticeRepository.findByProgram_Id(programId));
    }

    // By semester
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<Notice>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(noticeRepository.findBySemester_Id(semesterId));
    }

    // By visibility + faculty
    @GetMapping("/visibility/{visibleTo}/faculty/{facultyId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndFaculty(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long facultyId
    ) {
        return ResponseEntity.ok(noticeRepository.findByVisibleToAndFaculty_Id(visibleTo, facultyId));
    }

    // By visibility + program
    @GetMapping("/visibility/{visibleTo}/program/{programId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndProgram(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long programId
    ) {
        return ResponseEntity.ok(noticeRepository.findByVisibleToAndProgram_Id(visibleTo, programId));
    }

    // By visibility + semester
    @GetMapping("/visibility/{visibleTo}/semester/{semesterId}")
    public ResponseEntity<List<Notice>> getByVisibilityAndSemester(
            @PathVariable NoticeVisibility visibleTo,
            @PathVariable Long semesterId
    ) {
        return ResponseEntity.ok(noticeRepository.findByVisibleToAndSemester_Id(visibleTo, semesterId));
    }
}
