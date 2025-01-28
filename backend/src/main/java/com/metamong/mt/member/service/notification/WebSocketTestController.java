package com.metamong.mt.member.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTestController {

    private final WebSocketTestService webSocketTestService;

    @Autowired
    public WebSocketTestController(WebSocketTestService webSocketTestService) {
        this.webSocketTestService = webSocketTestService;
    }

    @PostMapping("/test/send-message")
    public ResponseEntity<String> sendMessage() {
        webSocketTestService.sendTestMessage();
        return ResponseEntity.ok("Test message sent to WebSocket");
    }
}
