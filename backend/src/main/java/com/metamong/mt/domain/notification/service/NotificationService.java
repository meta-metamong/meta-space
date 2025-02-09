package com.metamong.mt.domain.notification.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.repository.mybatis.NotificationMapper;

@Service
public class NotificationService {
	@Autowired
    private NotificationMapper notificationMapper;

    public void createNotification(Long receiverId, String message) {
        // 알림 생성
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setNotiMsg(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);  // 처음에는 읽지 않음

        // 알림 저장
        notificationMapper.insertNotification(notification);
    }

    // 알림을 웹소켓으로 전달하는 메소드 (필요시)
    public void sendNotificationToWebSocket(Long receiverId, String message) {
        // 웹소켓을 통해 실시간 알림을 보내는 로직
        // 웹소켓 서비스가 있다면 이를 호출하여 알림을 전달
    }
}
