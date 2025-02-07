package com.metamong.mt.domain.reservation.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.response.HourlyUsageDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;

@Repository
@Mapper
public interface ReservationMapper {
    List<ReservationResponseDto> findReservationByConsId(Long consId);

    ReservationResponseDto findReservationByRvtId(Long rvtId);

    List<ReservationInfoResponseDto> getTotalCount();
    
    List<HourlyUsageDto> getHourlyUsageCounts(ReservationRequestDto dto);
}