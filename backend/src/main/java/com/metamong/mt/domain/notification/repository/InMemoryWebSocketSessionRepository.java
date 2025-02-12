package com.metamong.mt.domain.notification.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class InMemoryWebSocketSessionRepository implements WebSocketSessionRepository {
    private static final Map<Long, WebSocketSession> SESSION_STORAGE_BY_MEM_ID = new ConcurrentHashMap<>();
    private static final Map<String, Long> MEM_ID_BY_SESSION_ID = new ConcurrentHashMap<>();
    
    @Override
    public void save(Long memId, WebSocketSession session) {
        SESSION_STORAGE_BY_MEM_ID.put(memId, session);
        MEM_ID_BY_SESSION_ID.put(session.getId(), memId);
    }
    @Override
    public Optional<WebSocketSession> findByMemId(Long memId) {
        return Optional.ofNullable(SESSION_STORAGE_BY_MEM_ID.get(memId));
    }
    @Override
    public void deleteBySessionId(String sessionId) {
        Long memId = MEM_ID_BY_SESSION_ID.get(sessionId);
        log.info("WebSocket connection closed - memId: {}, session Id: {}", memId, sessionId);
        SESSION_STORAGE_BY_MEM_ID.remove(memId);
        MEM_ID_BY_SESSION_ID.remove(sessionId);
    }
}
