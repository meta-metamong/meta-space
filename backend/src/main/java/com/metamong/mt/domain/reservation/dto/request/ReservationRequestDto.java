package com.metamong.mt.domain.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metamong.mt.domain.reservation.model.Reservation;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationRequestDto {
    private Long zoneId;
    private Long consId;

    @FutureOrPresent(message = "예약 날짜는 오늘 이후의 날짜만 가능합니다.")
    private LocalDate rvtDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageEndTime;

    @Min(value = 1, message = "이용 인원 수는 1 이상이어야 합니다.")
    private int usageCount;

    private int unitUsageTime;

    public Reservation toEntity() {
        return Reservation.builder().consId(this.consId).zoneId(this.zoneId).rvtDate(this.rvtDate)
                .usageStartTime(this.usageStartTime).usageEndTime(this.usageEndTime).usageCount(this.usageCount)
                .build();
    }

    public ReservationRequestDto(Long zoneId, Long consId, LocalDate rvtDate, LocalTime usageStartTime, LocalTime usageEndTime,
            int usageCount) {
        this.zoneId = zoneId;
        this.consId = consId;
        this.rvtDate = rvtDate;
        this.usageStartTime = usageStartTime;
        this.usageEndTime = usageEndTime;
        this.usageCount = usageCount;
    }
}
