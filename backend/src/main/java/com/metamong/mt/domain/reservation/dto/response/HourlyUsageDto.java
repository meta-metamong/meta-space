package com.metamong.mt.domain.reservation.dto.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourlyUsageDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageEndTime;

    private int totalUsageCount;
    
    public HourlyUsageDto(LocalTime usageStartTime, LocalTime usageEndTime, int totalUsageCount) {
        this.usageStartTime = usageStartTime;
        this.usageEndTime = usageEndTime;
        this.totalUsageCount = totalUsageCount;
    }
}
