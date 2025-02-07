package com.metamong.mt.domain.reservation.dto.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemainingCountResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageStartTime;
    private int remainUsageCount;
}
