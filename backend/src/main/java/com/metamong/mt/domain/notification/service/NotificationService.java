package com.metamong.mt.domain.notification.service;

public interface NotificationService {

    void sendMessage(Long memId, String message);
    
    void sendMessageToAll(String message);
}
