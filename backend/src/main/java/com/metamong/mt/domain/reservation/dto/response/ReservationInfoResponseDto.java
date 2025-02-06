package com.metamong.mt.domain.reservation.dto.response;

import lombok.Data;

@Data
public class ReservationInfoResponseDto {
	private Long rvtId;
	private Long fctId;
	private int totalCount;
}
