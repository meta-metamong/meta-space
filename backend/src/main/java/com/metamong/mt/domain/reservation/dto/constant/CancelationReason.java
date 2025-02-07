package com.metamong.mt.domain.reservation.dto.constant;

public enum CancelationReason {
    CHANGE("단순 변심"),
    MISTAKE("잘못된 예약"),
    DOUBLE("중복 예약"),
    PAYMENT("결제 문제"),
    WEATHER("날씨 문제"),
    FACILITY("시설 이용 불가");

    private final String description;

    CancelationReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
