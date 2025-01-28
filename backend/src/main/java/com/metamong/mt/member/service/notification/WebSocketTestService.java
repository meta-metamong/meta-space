package com.metamong.mt.member.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketTestService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketTestService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendTestMessage() {
        // /topic/test로 메시지 전송
        messagingTemplate.convertAndSend("/topic/test", "Test message from backend");
    }
}