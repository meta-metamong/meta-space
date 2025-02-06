package com.metamong.mt.domain.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.reservation.service.ReservationService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;
	
	@GetMapping("/members/{memberId}/reservations")
	public ResponseEntity<?> findReservationByConsId(@PathVariable Long memberId) {
		return ResponseEntity.ok(BaseResponse.of(reservationService.findReservationByConsId(memberId), HttpStatus.OK, "예약 목록 조회 성공"));
	}
	
	@GetMapping("/reservations/{reservationId}")
	public ResponseEntity<?> findReservationByRvtId(@PathVariable Long reservationId) {
		return ResponseEntity.ok(BaseResponse.of(reservationService.findReservationByRvtId(reservationId), HttpStatus.OK, "예약 상세 정보 불러오기 성공"));
	}
}
