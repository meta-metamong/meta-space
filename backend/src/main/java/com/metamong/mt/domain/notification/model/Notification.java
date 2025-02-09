package com.metamong.mt.domain.notification.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private Long notiId;        // 알림 아이디
    private Long receiverId;    // 수신자 아이디
    private String notiMsg;     // 알림 메시지
    private LocalDateTime createdAt;  // 등록 시간
    private Boolean isRead;     // 알림 읽음 여부
}
