package com.metamong.mt.domain.notification.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.notification.exception.FailedNotificationTransmissionException;
import com.metamong.mt.domain.notification.repository.WebSocketSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultWebSocketNotificationService implements WebSocketNotificationService {
    private final WebSocketSessionRepository webSocketSessionRepository;
    
    @Override
    public void sendMessage(Long memId, String message) {
        WebSocketSession session = this.webSocketSessionRepository.findByMemId(memId)
                .orElseThrow(() -> new FailedNotificationTransmissionException("회원 ID에 해당하는 웹 소켓 세션이 "
                        + "없습니다. memId=" + memId));
        
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            throw new FailedNotificationTransmissionException(e);
        }
    }
    
    @Override
    public void sendMessageToAll(String message) {
        this.webSocketSessionRepository.findAll().values()
                .forEach((session) -> {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new FailedNotificationTransmissionException(e);
                    }
                });
    }

    @Override
    public void saveSession(Long memId, WebSocketSession session) {
        this.webSocketSessionRepository.save(memId, session);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        this.webSocketSessionRepository.deleteBySessionId(sessionId);
    }
}
