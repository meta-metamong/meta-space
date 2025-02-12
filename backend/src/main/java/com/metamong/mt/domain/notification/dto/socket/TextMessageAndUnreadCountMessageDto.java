package com.metamong.mt.domain.notification.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TextMessageAndUnreadCountMessageDto {
    private final String message;
    private final int unreadCount;
}
