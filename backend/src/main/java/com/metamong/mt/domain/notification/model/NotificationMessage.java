package com.metamong.mt.domain.notification.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum NotificationMessage {
    // Reservation
    RESERVATION_CHECK_ONE_HOUR_REMAIN("예약이 1시간 남았습니다"),
    RESERVATION_CHECK_ONE_DAY_REMAIN("예약이 1일 남았습니다"),
    RESERVATION_CANCELATION("예약이 취소됐습니다"),
    NEW_RESERVATION("새로운 예약이 있습니다"),
    
    // Facility
    FACILITY_REGISTRATION_ACCEPTED("시설 등록이 승인됐습니다"),
    FACILITY_REGISTRATION_REJECTED("시설 등록이 거절됐습니다"),
    FACILITY_DELETION_ACCEPTED("시설 삭제 요청이 승인되었습니다"),
    FACILITY_DELETION_REJECTED("시설 삭제 요청이 반려되었습니다"),
    
    // Refund
    REFUND_REJECTED("환불 요청이 거절됐습니다"),
    NEW_REFUND_REQUEST("새로운 환불 요청이 있습니다"),
    REFUND_ACCEPT("환불 요청이 승인됐습니다");
    
    private final String message;
}
