package com.metamong.mt.domain.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metamong.mt.domain.reservation.dto.constant.PayState;

import lombok.Getter;

@Getter
public class FctReservationResponseDto {
    private LocalDate rvtDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageEndTime;

    private String memName;
    private String zoneName;
    private int usageCount;

    private PayState payState;
    private String rvtState;
    private int payPrice;

    public void setPayState(String payState) {
        this.payState = PayState.fromCode(payState);
        this.rvtState = this.payState.getReservationState(this.rvtDate).getDescription(); // 예약 상태 설정
    }
}
