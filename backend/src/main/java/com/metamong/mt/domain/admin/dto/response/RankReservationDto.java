package com.metamong.mt.domain.admin.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankReservationDto {
    private String fctName; // 시설 이름
    private int totRvt;     // 예약 건수
}
