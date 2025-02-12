package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekHourReservationDto {

    private String rvtHour;   // 예약 시간 (24시간 형식)
    private int rvtCount;     // 예약 개수
}
