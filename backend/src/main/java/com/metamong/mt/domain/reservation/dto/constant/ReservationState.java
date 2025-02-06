package com.metamong.mt.domain.reservation.dto.constant;

public enum ReservationState {
    COMPLETED("이용 완료"),
    RESERVED("예약 완료"),
    CANCELED("예약 취소");

    private final String description;

    ReservationState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
