package com.metamong.mt.domain.notification.service;

import java.util.List;

import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;

public interface NotificationService {

    void sendMessage(Long receiverId, String message);
    
    int countUnreadNotificationsByReceiverId(Long receiverId);
    
    List<NotificationResponseDto> findNotifications(Long receiverId, boolean includeRead);
    
    void readNotifications(List<Long> notificationIds);
    
    void deleteById(Long notiId);
}
