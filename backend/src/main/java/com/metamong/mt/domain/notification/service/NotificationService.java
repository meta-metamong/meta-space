package com.metamong.mt.domain.notification.service;

import java.util.List;

import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;
import com.metamong.mt.domain.notification.model.NotificationMessage;

public interface NotificationService {

    void sendMessage(Long receiverId, NotificationMessage notificationType);
    
    int countUnreadNotificationsByReceiverId(Long receiverId);
    
    List<NotificationResponseDto> findNotifications(Long receiverId, boolean includeRead);
    
    void readNotification(Long notificationId);
    
    void deleteById(Long notiId);
}
