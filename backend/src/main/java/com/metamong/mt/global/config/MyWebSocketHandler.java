/*package com.metamong.mt.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.member.controller.MemberController;
import com.metamong.mt.domain.member.dto.request.MessageRequestDto;
import com.metamong.mt.domain.member.dto.response.MessageResponseDto;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MemberController memberController;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    // WebSocket 연결 시 세션을 추가
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	memberController.addSession(session); // WebSocket 세션을 BoardController에 추가
    }

    // 클라이언트로부터 받은 메시지를 처리하고, 클라이언트에게 JSON 응답을 보냄
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // 클라이언트에서 받은 메시지를 JSON으로 변환
        MessageRequestDto request = objectMapper.readValue(message.getPayload(), MessageRequestDto.class);

        // 서버로부터의 응답 메시지 생성 (클라이언트의 userId 포함)
        MessageResponseDto response = new MessageResponseDto(request.getFrom(), request.getText(), request.getUserId());
        String jsonResponse = objectMapper.writeValueAsString(response); // 객체 → JSON 변환

        // 모든 세션에 메시지 전송
        for (WebSocketSession webSocketSession : memberController.getAllSessions()) {
            if (webSocketSession.isOpen()) { // 세션이 열려있는지 확인
                webSocketSession.sendMessage(new TextMessage(jsonResponse));
            }
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	memberController.removeSession(session);
        System.out.println("WebSocket 연결 종료: " + session.getId());
    }
}
*/
package com.metamong.mt.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.member.controller.MemberController;
import com.metamong.mt.domain.member.dto.request.MessageRequestDto;
import com.metamong.mt.domain.member.dto.response.MessageResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MemberController memberController;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    // WebSocket 연결 시 세션을 추가
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        memberController.addSession(session); // WebSocket 세션을 MemberController에 추가
    }

    // 클라이언트로부터 받은 메시지를 처리하고, 클라이언트에게 JSON 응답을 보냄
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // 클라이언트에서 받은 메시지를 JSON으로 변환
        MessageRequestDto request = objectMapper.readValue(message.getPayload(), MessageRequestDto.class);

        // 서버로부터의 응답 메시지 생성 (클라이언트의 userId 포함)
        MessageResponseDto response = new MessageResponseDto(request.getFrom(), request.getText(), request.getUserId());
        String jsonResponse = objectMapper.writeValueAsString(response); // 객체 → JSON 변환

        // 모든 세션 가져오기
        if ("Admin".equals(request.getFrom())) {
            // 1. Admin이 보낸 메시지는 모든 세션에게 전송
            for (WebSocketSession webSocketSession : memberController.getAllSessions()) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(jsonResponse));
                }
            }
        } else {
            // 2. Admin이 아닌 사용자의 메시지는 "Admin" 세션에게만 전송
            List<WebSocketSession> adminSessions = getAdminSessions();
            for (WebSocketSession adminSession : adminSessions) {
                if (adminSession.isOpen()) {
                    adminSession.sendMessage(new TextMessage(jsonResponse));
                }
            }
        }
    }

    // "Admin" 역할을 가진 WebSocket 세션 찾기
    private List<WebSocketSession> getAdminSessions() {
    	for(WebSocketSession session : memberController.getAllSessions()) {
            session.getAttributes().forEach((key, value) -> {
                System.out.println("Key: " + key + ", Value: " + value);
            });
    	}
        return memberController.getAllSessions().stream()
                .filter(session -> "admin".equals(session.getAttributes().get("userId")))
                .collect(Collectors.toList());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        memberController.removeSession(session);
        System.out.println("WebSocket 연결 종료: " + session.getId());
    }
}
