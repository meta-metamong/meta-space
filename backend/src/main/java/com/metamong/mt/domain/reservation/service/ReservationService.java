package com.metamong.mt.domain.reservation.service;

import java.util.List;

import com.metamong.mt.domain.reservation.model.Reservation;

public interface ReservationService {
	List<Reservation> findReservationByConsId(int consId);
}
