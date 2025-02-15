package com.metamong.mt.domain.reservation.dto.constant;

import java.time.LocalDate;

public enum PayState {
    PAID("P", "결제됨"),
    REFUND_REQUESTED("Q", "환불 요청"),
    REFUNDED("R", "환불됨"),
    REFUND_REJECTED("N", "환불 반려");

    private final String code;
    private final String description;

    PayState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PayState fromCode(String code) {
        for (PayState status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    // 예약 상태를 반환하는 메서드
    public ReservationState getReservationState(LocalDate rvtDate) {
        LocalDate today = LocalDate.now();

        if (this == PAID) {
            return rvtDate.isBefore(today) ? ReservationState.COMPLETED : ReservationState.RESERVED;
        } else {
            return ReservationState.CANCELED;
        }
    }
}
