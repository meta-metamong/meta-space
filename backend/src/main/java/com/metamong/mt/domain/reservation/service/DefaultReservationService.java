package com.metamong.mt.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.repository.jpa.ReservationRepository;
import com.metamong.mt.domain.reservation.repository.mybatis.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultReservationService implements ReservationService {
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationResponseDto> findReservationByConsId(Long consId) {
        return this.reservationMapper.findReservationByConsId(consId);
    }

    @Override
    public ReservationResponseDto findReservationByRvtId(Long rvtId) {
        return this.reservationMapper.findReservationByRvtId(rvtId);
    }

    @Override
    public List<ReservationInfoResponseDto> getTotalCount() {
        return this.reservationMapper.getTotalCount();
    }
}
