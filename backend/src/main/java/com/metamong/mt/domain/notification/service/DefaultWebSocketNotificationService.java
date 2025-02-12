package com.metamong.mt.domain.notification.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.notification.dto.mapper.NotificationListMapperDto;
import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;
import com.metamong.mt.domain.notification.exception.FailedNotificationTransmissionException;
import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.repository.WebSocketSessionRepository;
import com.metamong.mt.domain.notification.repository.jpa.NotificationRepository;
import com.metamong.mt.domain.notification.repository.mybatis.NotificationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultWebSocketNotificationService implements WebSocketNotificationService {
    private final WebSocketSessionRepository webSocketSessionRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    
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
    public void sendMessage(Long memId, Object message) {
        try {
            sendMessage(memId, this.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new FailedNotificationTransmissionException(e);
        }
    }
    
    @Override
    public void sendMessageToAll(String message) {
        Map<Long, WebSocketSession> sessionsByMemId = this.webSocketSessionRepository.findAll();
        if (log.isTraceEnabled()) {
            log.trace("memIds={}", sessionsByMemId.keySet());
        }
        sessionsByMemId.values()
                .forEach((session) -> {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new FailedNotificationTransmissionException(e);
                    }
                });
    }
    
    @Override
    public void sendMessageToAll(Object message) {
        try {
            sendMessageToAll(this.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new FailedNotificationTransmissionException(e);
        }
    }
    
    private Notification saveNotification(Long receiverId, String message) {
        Notification newNotification = new Notification(
                receiverId,
                message,
                LocalDateTime.now(),
                'N'
        );
        return this.notificationRepository.save(newNotification);
    }

    @Override
    public void saveSession(Long memId, WebSocketSession session) {
        this.webSocketSessionRepository.save(memId, session);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        this.webSocketSessionRepository.deleteBySessionId(sessionId);
    }
    
    @Override
    public List<NotificationResponseDto> findNotifications(Long receiverId, boolean includeRead) {
        return this.notificationMapper.findNotificationsByReceiverId(new NotificationListMapperDto(receiverId, includeRead))
                .stream()
                .map(NotificationResponseDto::of)
                .toList();
    }
}
