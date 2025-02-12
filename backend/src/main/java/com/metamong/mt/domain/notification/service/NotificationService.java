package com.metamong.mt.domain.notification.service;

import java.util.List;

import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;

public interface NotificationService {

    void sendMessage(Long receiverId, String message);

    void sendMessage(Long receiverId, Object message);
    
    void sendMessageToAll(String message);
    
    void sendMessageToAll(Object message);
    
    List<NotificationResponseDto> findNotifications(Long receiverId, boolean includeRead);
}
