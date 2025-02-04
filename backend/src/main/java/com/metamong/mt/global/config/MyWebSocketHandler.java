package com.metamong.mt.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.member.controller.MemberController;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MemberController boardController;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    // WebSocket 연결 시 세션을 추가
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        boardController.addSession(session); // WebSocket 세션을 BoardController에 추가
    }

    // 클라이언트로부터 받은 메시지를 처리하고, 클라이언트에게 JSON 응답을 보냄
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // 클라이언트에서 받은 메시지를 JSON으로 변환
        MessageRequest request = objectMapper.readValue(message.getPayload(), MessageRequest.class);

        // 서버로부터의 응답 메시지 생성 (클라이언트의 userId 포함)
        MessageResponse response = new MessageResponse("Server", request.getText(), request.getUserId());
        String jsonResponse = objectMapper.writeValueAsString(response); // 객체 → JSON 변환

        // 모든 세션에 메시지 전송
        for (WebSocketSession webSocketSession : boardController.getAllSessions()) {
            if (webSocketSession.isOpen()) { // 세션이 열려있는지 확인
                webSocketSession.sendMessage(new TextMessage(jsonResponse));
            }
        }
    }

    // 클라이언트로부터 받은 메시지 구조
    private static class MessageRequest {
        private String text;
        private String userId;

        // Getter, Setter
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    // JSON 메시지 구조를 위한 내부 클래스 (응답)
    private static class MessageResponse {
        private String from;
        private String text;
        private String userId;

        public MessageResponse(String from, String text, String userId) {
            this.from = from;
            this.text = text;
            this.userId = userId;
        }

        // Getter, Setter
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
