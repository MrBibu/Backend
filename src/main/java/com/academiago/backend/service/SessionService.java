package com.academiago.backend.service;

import com.academiago.backend.model.Session;
import com.academiago.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() { return sessionRepository.findAll(); }
    public Optional<Session> getSessionById(Integer id) { return sessionRepository.findById(id); }
    public Session createSession(Session session) { return sessionRepository.save(session); }

    public Session updateSession(Integer id, Session updatedSession) {
        return sessionRepository.findById(id).map(s -> {
            s.setUser(updatedSession.getUser());
            s.setTokenHash(updatedSession.getTokenHash());
            s.setExpiresAt(updatedSession.getExpiresAt());
            s.setActive(updatedSession.getActive());
            return sessionRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public void deleteSession(Integer id) { sessionRepository.deleteById(id); }
}
