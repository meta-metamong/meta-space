package com.metamong.mt.domain.reservation.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.facility.repository.jpa.ZoneRepository;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.request.SelectedInfoRequestDto;
import com.metamong.mt.domain.reservation.dto.response.HourlyUsageDto;
import com.metamong.mt.domain.reservation.dto.response.RemainingCountResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.exception.ReservationDuplicatedException;
import com.metamong.mt.domain.reservation.exception.ReservationNotFoundException;
import com.metamong.mt.domain.reservation.model.Reservation;
import com.metamong.mt.domain.reservation.repository.jpa.ReservationRepository;
import com.metamong.mt.domain.reservation.repository.mybatis.ReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultReservationService implements ReservationService {
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final ZoneRepository zoneRepository;
    private final FacilityRepository facilityRepository;

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
    public List<RemainingCountResponseDto> getRemainingUsageCount(SelectedInfoRequestDto dto) {
        Facility fctInfo = facilityRepository.findById(dto.getFctId())
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 존재하지 않습니다."));
        LocalTime openTime = fctInfo.getFctOpenTime();
        LocalTime closeTime = fctInfo.getFctCloseTime();
        int unitTime = fctInfo.getUnitUsageTime();
        
        List<HourlyUsageDto> reservedTimes = reservationMapper.getReservedTimes(dto);
        
        List<RemainingCountResponseDto> availableTimes = new ArrayList<>();
        int maxUserCount = zoneRepository.findMaxUserCountByZoneId(dto.getZoneId());
        LocalTime currentTime = openTime;
        
        while (currentTime.plusMinutes(unitTime).isBefore(closeTime) || currentTime.plusMinutes(unitTime).equals(closeTime)) {
            LocalTime nextTime = currentTime.plusMinutes(unitTime);
            RemainingCountResponseDto timeInfo = new RemainingCountResponseDto();
            timeInfo.setUsageStartTime(currentTime);

            // 기본적으로 최대 인원 수로 설정
            int remainingCapacity = maxUserCount;

            // 현재 시간 슬롯에 해당하는 예약된 인원 차감
            for (HourlyUsageDto usage : reservedTimes) {
                if (usage.getUsageStartTime().isBefore(nextTime) && usage.getUsageEndTime().isAfter(currentTime)) {
                    remainingCapacity -= usage.getTotalUsageCount();
                }
            }

            // 남은 인원 저장
            timeInfo.setRemainUsageCount(Math.max(remainingCapacity, 0));
            availableTimes.add(timeInfo);
            currentTime = nextTime;
        }
        return availableTimes;
    }

    @Override
    @Transactional
    public Reservation saveReservation(ReservationRequestDto dto) {
        int maxUserCount = zoneRepository.findMaxUserCountByZoneId(dto.getZoneId());
        
        // 각 시간대별 예약된 인원 조회
        List<HourlyUsageDto> existingReservations = reservationMapper.getHourlyUsageCounts(dto);
        Map<LocalTime, Integer> reservedCountMap = new HashMap<>();
        for (HourlyUsageDto hourUsage : existingReservations) {
            LocalTime startTime = hourUsage.getUsageStartTime();
            LocalTime endTime = hourUsage.getUsageEndTime();
            int reservedCount = hourUsage.getTotalUsageCount();
            
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
                throw new ReservationDuplicatedException("예약 가능한 인원 수를 초과하였습니다.");
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
