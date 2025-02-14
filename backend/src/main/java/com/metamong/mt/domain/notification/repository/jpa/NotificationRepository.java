package com.metamong.mt.domain.notification.repository.jpa;

import com.metamong.mt.domain.notification.model.Notification;

public interface NotificationRepository {
    
    int countNotReadNotificationsByReceiverId(Long receiverId);
    
    Notification save(Notification notification);
    
    void deleteById(Long notiId);
    
    void readByNotificationId(Long notificationId);
}
