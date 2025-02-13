package com.metamong.mt.domain.admin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesExportDto {
    private Long fctId;
    private String fctName;
    private Long zoneId;
    private String zoneName;
    private LocalDate payDate;
    private BigDecimal payPrice;
    private String payMethod;
}
