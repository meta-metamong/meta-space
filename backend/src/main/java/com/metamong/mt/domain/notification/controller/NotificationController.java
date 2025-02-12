package com.metamong.mt.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.notification.dto.response.NotificationResponseDto;
import com.metamong.mt.domain.notification.service.NotificationService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/members/{memId}/unread-notification-count")
    public ResponseEntity<BaseResponse<Integer>> countUnread(@PathVariable("memId") Long memId) {
        return ResponseEntity.ok(
                BaseResponse.of(this.notificationService.countUnreadNotificationsByReceiverId(memId), HttpStatus.OK)
        );
    }
    
    @GetMapping("/members/{memId}/notifications")
    public ResponseEntity<BaseResponse<List<NotificationResponseDto>>> findNotifications(
            @PathVariable("memId") Long memId,
            @RequestParam(value = "include-read", defaultValue = "true") boolean includeRead            
    ) {
        return ResponseEntity.ok(
                BaseResponse.of(this.notificationService.findNotifications(memId, includeRead), HttpStatus.OK)
        );
    }
    
    @PatchMapping("/notifications/read")
    public ResponseEntity<BaseResponse<Void>> readNotifications(@RequestBody List<Long> notificationIds) {
        this.notificationService.readNotifications(notificationIds);
        return new ResponseEntity<>(
                BaseResponse.of(HttpStatus.NO_CONTENT),
                HttpStatus.NO_CONTENT
        );
    }
}
