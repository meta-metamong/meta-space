package com.metamong.mt.domain.reservation.service;

import java.util.List;

import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.request.SelectedInfoRequestDto;
import com.metamong.mt.domain.reservation.dto.response.RemainingCountResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.model.Reservation;

public interface ReservationService {
    List<ReservationResponseDto> findReservationByConsId(Long consId);

    ReservationResponseDto findReservationByRvtId(Long rvtId);

    List<ReservationInfoResponseDto> getTotalCount();
    
    List<RemainingCountResponseDto> getRemainingUsageCount(SelectedInfoRequestDto dto);
    
    void saveReservation(ReservationRequestDto dto);
    
    void cancelReservation(Long rvtId, CancelRequestDto dto);
}
