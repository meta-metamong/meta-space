package com.metamong.mt.domain.reservation.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.facility.repository.jpa.ZoneRepository;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.exception.ReservationDuplicatedException;
import com.metamong.mt.domain.reservation.exception.ReservationNotFoundException;
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
    private final ZoneRepository zoneRepository;

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

    @Override
    @Transactional
    public Reservation saveReservation(ReservationRequestDto dto) {
        int maxUserCount = zoneRepository.findMaxUserCountByZoneId(dto.getZoneId());
        
        // 각 시간대별 예약된 인원 조회
        List<Object[]> existingReservations = reservationRepository.getHourlyUsageCounts(dto.getRvtDate(), dto.getUsageStartTime(), dto.getUsageEndTime(), dto.getZoneId());
        Map<LocalTime, Integer> reservedCountMap = new HashMap<>();
        for (Object[] row : existingReservations) {
            LocalTime startTime = (LocalTime) row[0];
            LocalTime endTime = (LocalTime) row[1];
            int reservedCount = ((Number) row[2]).intValue();
            
            // 예약된 시간 동안 모든 단위 시간별 인원을 기록
            LocalTime time = startTime;
            while (time.isBefore(endTime)) {
                reservedCountMap.put(time, reservedCountMap.getOrDefault(time, 0) + reservedCount);
                time = time.plusHours(1);
            }
        }

        // 새로 예약하려는 시간대별 인원 체크
        LocalTime checkTime = dto.getUsageStartTime();
        while (checkTime.isBefore(dto.getUsageEndTime())) {
            int currentReserved = reservedCountMap.getOrDefault(checkTime, 0);
            int newTotal = currentReserved + dto.getUsageCount();

            if (newTotal > maxUserCount) {
                throw new ReservationDuplicatedException();
            }

            checkTime = checkTime.plusHours(1);
        }
        
        return this.reservationRepository.save(dto.toEntity());
    }

    @Override
    public void cancelReservation(Long rvtId, CancelRequestDto dto) {
        Reservation reservation = this.reservationRepository.findById(rvtId)
                .orElseThrow(() -> new ReservationNotFoundException(rvtId, "예약을 찾을 수 없습니다."));
        reservation.setRvtCancelationReason(dto.getRvtCancelationReason());
    }
}
