package com.metamong.mt.domain.notification.repository;

import java.util.Optional;

import org.springframework.web.socket.WebSocketSession;

/**
 * 웹 소켓 세션을 저장하고 관리하는 객체의 행위를 정의한 인터페이스
 */
public interface WebSocketSessionRepository {

    /**
     * 웹 소켓 세션 저장. 회원의 ID와 웹 소켓 세션을 바인딩한다.
     * 
     * @param memId 웹 소켓 세션과 바인딩할 회원의 ID
     * @param session 웹 소켓 세션
     */
    void save(Long memId, WebSocketSession session);
    
    /**
     * 회원의 ID에 바인딩된 웹 소켓 세션 불러오기
     * 
     * @param memId 회원 ID
     * @return memId와 바인딩된 웹 소켓 세션
     */
    Optional<WebSocketSession> findByMemId(Long memId);
    
    /**
     * 웹 소켓 세션 ID로 세션 삭제. 회원 ID 정보도 삭제된다.
     * 
     * @param sessionId 웹 소켓 세션 ID
     */
    void deleteBySessionId(String sessionId);
}
