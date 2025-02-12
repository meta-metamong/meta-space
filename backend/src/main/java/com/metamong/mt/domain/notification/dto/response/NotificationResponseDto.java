package com.metamong.mt.domain.notification.dto.response;

import java.time.LocalDateTime;

import com.metamong.mt.domain.notification.model.Notification;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Builder
@Getter
@ToString
public class NotificationResponseDto {
    private final Long notiId;
    private final Long receiverId;
    private final String notiMsg;
    private final LocalDateTime createdAt;
    private final boolean read;
    
    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                .notiId(notification.getNotiId())
                .receiverId(notification.getReceiverId())
                .notiMsg(notification.getNotiMsg().getMessage())
                .createdAt(notification.getCreatedAt())
                .read(notification.getIsRead() == 'Y')
                .build();
    }
}
