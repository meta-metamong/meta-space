package com.metamong.mt.domain.notification.dto.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class NotificationListMapperDto {
    private Long receiverId;
    private boolean includeRead;
}
