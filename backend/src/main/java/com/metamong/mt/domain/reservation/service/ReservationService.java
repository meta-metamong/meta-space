package com.metamong.mt.domain.reservation.service;

import java.util.List;

import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;

public interface ReservationService {
	List<ReservationResponseDto> findReservationByConsId(int consId);
	ReservationResponseDto findReservationByRvtId(int rvtId);
}
