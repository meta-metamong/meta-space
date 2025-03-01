package com.metamong.mt.domain.reservation.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.reservation.dto.mapper.FindConsIdWithReservationTimeMapperDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.request.SelectedInfoRequestDto;
import com.metamong.mt.domain.reservation.dto.response.FctReservationResponseDto;
import com.metamong.mt.domain.reservation.dto.response.HourlyUsageDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;

@Repository
@Mapper
public interface ReservationMapper {
    int countReservations(Long consId);
    
    List<ReservationResponseDto> findReservationByConsId(@Param("consId") Long consId, @Param("startRow") int startRow, @Param("endRow") int endRow);

    ReservationResponseDto findReservationByRvtId(Long rvtId);

    List<ReservationInfoResponseDto> getTotalCount();
    
    List<ReservationInfoResponseDto> getTopFacilities();
    
    List<HourlyUsageDto> getHourlyUsageCounts(ReservationRequestDto dto);
    
    List<HourlyUsageDto> getReservedTimes(SelectedInfoRequestDto dto);
    
    int findReportByReporterIdAndReportedId(Long reporterId, Long reportedId); 

    List<Long> findConsIdWithLeftReservationTime(@Param("dto") FindConsIdWithReservationTimeMapperDto dto);
    
    Optional<Long> findProvIdByRvtId(@Param("rvtId") Long rvtId);

    List<FctReservationResponseDto> findReservationByFctId(Long fctId);
}