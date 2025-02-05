package com.metamong.mt.domain.reservation.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;

@Repository
@Mapper
public interface ReservationMapper {
	List<ReservationResponseDto> findReservationByConsId(int consId);
	ReservationResponseDto findReservationByRvtId(int rvtId);
}