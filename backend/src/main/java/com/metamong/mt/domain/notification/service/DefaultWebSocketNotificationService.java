package com.metamong.mt.domain.notification.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.notification.dto.mapper.NotificationListMapperDto;
import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;
import com.metamong.mt.domain.notification.dto.socket.TextMessageAndUnreadCountMessageDto;
import com.metamong.mt.domain.notification.exception.FailedNotificationTransmissionException;
import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.model.NotificationMessage;
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
    public void sendMessage(Long receiverId, NotificationMessage notiMsg) {
        saveNotification(receiverId, notiMsg);
        this.webSocketSessionRepository.findByMemId(receiverId)
                .ifPresent((session) -> {
                    try {
                        int unreadCount = this.countUnreadNotificationsByReceiverId(receiverId);
                        String messageInJson =
                                this.objectMapper.writeValueAsString(new TextMessageAndUnreadCountMessageDto(notiMsg.getMessage(), unreadCount));
                        session.sendMessage(new TextMessage(messageInJson));
                    } catch (IOException e) {
                        throw new FailedNotificationTransmissionException(e);
                    }
                });
    }
    
    private Notification saveNotification(Long receiverId, NotificationMessage notiMsg) {
        Notification newNotification = new Notification(
                receiverId,
                notiMsg,
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
    public int countUnreadNotificationsByReceiverId(Long receiverId) {
        return this.notificationRepository.countNotReadNotificationsByReceiverId(receiverId);
    }
    
    @Override
    public List<NotificationResponseDto> findNotifications(Long receiverId, boolean includeRead) {
        this.notificationRepository.readAllNotificationsByReceiverId(receiverId);
        return this.notificationMapper.findNotificationsByReceiverId(new NotificationListMapperDto(receiverId, includeRead))
                .stream()
                .map(NotificationResponseDto::of)
                .toList();
    }
    
    @Override
    public void readNotifications(List<Long> notificationIds) {
        this.notificationMapper.readByNotificationIds(notificationIds);
    }
    
    @Override
    public void deleteById(Long notiId) {
        this.notificationRepository.deleteById(notiId);
    }
}
