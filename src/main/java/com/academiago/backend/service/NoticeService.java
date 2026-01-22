package com.academiago.backend.service;

import com.academiago.backend.model.Notice;
import com.academiago.backend.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() { return noticeRepository.findAll(); }
    public Optional<Notice> getNoticeById(Integer id) { return noticeRepository.findById(id); }
    public Notice createNotice(Notice notice) { return noticeRepository.save(notice); }

    public Notice updateNotice(Integer id, Notice updatedNotice) {
        return noticeRepository.findById(id).map(n -> {
            n.setTitle(updatedNotice.getTitle());
            n.setBody(updatedNotice.getBody());
            n.setFaculty(updatedNotice.getFaculty());
            n.setSemester(updatedNotice.getSemester());
            return noticeRepository.save(n);
        }).orElseThrow(() -> new RuntimeException("Notice not found"));
    }

    public void deleteNotice(Integer id) {
        noticeRepository.deleteById(id);
    }
}
