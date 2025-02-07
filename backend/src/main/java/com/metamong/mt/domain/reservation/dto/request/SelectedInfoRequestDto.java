package com.metamong.mt.domain.reservation.dto.request;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class SelectedInfoRequestDto {
    private LocalDate rvtDate;
    private Long fctId;
    private Long zoneId;
}
