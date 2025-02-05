package com.metamong.mt.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.reservation.model.Reservation;
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
	public List<Reservation> findReservationByConsId(int consId) {
		return this.reservationMapper.findReservationByConsId(consId);
	}

}
