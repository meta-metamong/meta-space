package com.metamong.mt.global.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static final Map<Long, WebSocketSession> SESSION_STORAGE_BY_MEM_ID = new ConcurrentHashMap<>();
    private static final Map<String, Long> MEM_ID_BY_SESSION_ID = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long memId = (Long) session.getAttributes().get("memId");
        log.info("WebSocket connection established - memId: {}, session Id={}", memId, session.getId());
        SESSION_STORAGE_BY_MEM_ID.put(memId, session);
        MEM_ID_BY_SESSION_ID.put(session.getId(), memId);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage - session={}", session.getId());
        log.info("memId={}", MEM_ID_BY_SESSION_ID.get(session.getId()));
        log.info("message={}", message);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String sessionId = session.getId();
        Long memId = MEM_ID_BY_SESSION_ID.get(sessionId);
        
        log.info("WebSocket connection closed - memId: {}, session Id: {}", memId, session.getId());
        log.info("status={}", status);
        
        SESSION_STORAGE_BY_MEM_ID.remove(memId);
        MEM_ID_BY_SESSION_ID.remove(sessionId);
    }
/*
    @Autowired
    private MemberController memberController;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    
    // WebSocket 연결 시 세션을 추가
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Controller.addSession(session);
        updateOnlineUsers(); 
    }

    // 클라이언트로부터 받은 메시지를 처리하고, 클라이언트에게 JSON 응답을 보냄
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // 클라이언트에서 받은 메시지를 JSON으로 변환
        MessageRequestDto request = objectMapper.readValue(message.getPayload(), MessageRequestDto.class);
        System.out.println("request.getText(): " + request.getTo()); 
        
        // 서버로부터의 응답 메시지 생성
        MessageResponseDto response = new MessageResponseDto(request.getFrom(), request.getText(), request.getUserId(),"chatMessage");
        System.out.println("request.getText(): " + request.getText()); 
        System.out.println("MessageResponseDto 생성: " + response.toString());
        String jsonResponse = objectMapper.writeValueAsString(response); // 객체 → JSON 변환
        System.out.println("Sending message to client: " + jsonResponse);

        if ("admin".equals(request.getUserId())) {
            String targetUserId = request.getTo(); // ✅ 채팅하기 클릭된 대상 userId 가져오기

            for (WebSocketSession webSocketSession : memberController.getAllSessions()) {
                String sessionUserId = (String) webSocketSession.getAttributes().get("userId");

                if (webSocketSession.isOpen() && targetUserId.equals(sessionUserId)) { // ✅ 해당 userId에게만 전송
                    System.out.println("관리자가 [" + targetUserId + "] 에게 메시지 전송");
                    webSocketSession.sendMessage(new TextMessage(jsonResponse));
                }
            }
        }
        else {
            // 2. Admin이 아닌 사용자의 메시지는 "Admin" 세션에게만 전송
            List<WebSocketSession> adminSessions = getAdminSessions();
            for (WebSocketSession adminSession : adminSessions) {
                if (adminSession.isOpen()) {
                    adminSession.sendMessage(new TextMessage(jsonResponse));
                }
            }
        }
    }
    
    private void updateOnlineUsers() throws IOException {
        List<String> onlineUsers = memberController.getAllSessions().stream()
            .map(session -> (String) session.getAttributes().get("userId"))
            .collect(Collectors.toList());

        // ✅ JSON 메시지 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(Map.of("type", "updateOnlineUsers", "users", onlineUsers));
        System.out.println(message);
        // ✅ 모든 클라이언트에게 전송
        for (WebSocketSession session : memberController.getAllSessions()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
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
        updateOnlineUsers();
        System.out.println("WebSocket 연결 종료: " + session.getId());
    }
    */
}
