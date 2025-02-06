package com.metamong.mt.domain.reservation.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.model.Reservation;

@Repository
@Mapper
public interface ReservationMapper {
	List<Reservation> findReservationByConsId(int consId);
	List<ReservationInfoResponseDto> getTotalCount();
}