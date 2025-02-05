package com.metamong.mt.domain.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationResponseDto {
	private int rvtId;
	private LocalDate rvtDate;
	private String fctName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime usageStartTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime usageEndTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime cancelableTime;
	
	private String zoneName;
	private String fctAddress;
	private String fctDetailAddress;
	private int usageCount;
	
	public void setUsageStartTime(LocalTime usageStartTime) {
        this.usageStartTime = usageStartTime;
        this.cancelableTime = usageStartTime.minusHours(1);
    }
}
