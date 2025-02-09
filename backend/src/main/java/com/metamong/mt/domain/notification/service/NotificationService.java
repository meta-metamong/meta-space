package com.metamong.mt.domain.notification.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.repository.jpa.NotificationRepository;
import com.metamong.mt.domain.notification.repository.mybatis.NotificationMapper;
import com.metamong.mt.global.config.MyWebSocketHandler;

@Service
public class NotificationService {
	@Autowired
    private NotificationMapper notificationMapper;
	
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;

    @Transactional
    public void createNotification(Long receiverId, String message) {
        // 알림 생성
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setNotiMsg(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);  // 처음에는 읽지 않음

        // 알림 저장
        //notificationMapper.insertNotification(notification);
        notificationRepository.save(notification);
        notificationRepository.flush();
        
        // 저장 후 실시간 알림 전송
        int unreadCount = 1;//notificationRepository.countByReceiverIdAndIsReadFalse(receiverId);
        try {
        	myWebSocketHandler.sendNotification(receiverId, unreadCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 알림을 웹소켓으로 전달하는 메소드 (필요시)
    public void sendNotificationToWebSocket(Long receiverId, String message) {
        // 웹소켓을 통해 실시간 알림을 보내는 로직
        // 웹소켓 서비스가 있다면 이를 호출하여 알림을 전달
    }
}
