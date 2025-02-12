package com.metamong.mt.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankPaymentDto {
    private String fctName; // 시설 이름
    private int totPay;     // 예약 건수
}
