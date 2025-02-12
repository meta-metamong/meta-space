package com.metamong.mt.domain.notification;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketNotificationService extends NotificationService {
    
    /**
     * 웹 소켓 세션 저장. 회원의 ID와 웹 소켓 세션을 바인딩한다.
     * 
     * @param memId 웹 소켓 세션과 바인딩할 회원의 ID
     * @param session 웹 소켓 세션
     */
    void saveSession(Long memId, WebSocketSession session);
    
    /**
     * 웹 소켓 세션 ID로 세션 삭제. 회원 ID 정보도 삭제된다.
     * 
     * @param sessionId 웹 소켓 세션 ID
     */
    void deleteBySessionId(String sessionId);
}
