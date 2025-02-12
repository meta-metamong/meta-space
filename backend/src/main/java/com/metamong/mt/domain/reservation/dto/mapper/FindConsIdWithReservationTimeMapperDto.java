package com.metamong.mt.domain.reservation.dto.mapper;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FindConsIdWithReservationTimeMapperDto {
    private int leftReservationTimeInHour;
    private LocalDateTime criteriaTime;
}
