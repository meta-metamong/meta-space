package com.metamong.mt.member.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String message) {
        // "/topic/notifications" 채널로 메시지 전송
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
