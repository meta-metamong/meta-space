package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekReservationDto {
	private String fctId;
    private int sun;
    private int mon;
    private int tues;
    private int wednes;
    private int thurs;
    private int fri;
    private int satur;
}
