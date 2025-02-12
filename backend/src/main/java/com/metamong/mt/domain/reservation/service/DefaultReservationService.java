package com.metamong.mt.domain.reservation.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.notification.model.NotificationMessage;
import com.metamong.mt.domain.notification.service.NotificationService;
import com.metamong.mt.domain.payment.model.Payment;
import com.metamong.mt.domain.payment.service.PaymentService;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationListRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationNPaymentRequestDto;
import com.metamong.mt.domain.reservation.dto.request.SelectedInfoRequestDto;
import com.metamong.mt.domain.reservation.dto.response.FctReservationResponseDto;
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
    private final FacilityRepository facilityRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private static final int PAGE_SIZE = 5;
    
    @Override
    public ReservationListRequestDto<ReservationResponseDto> findReservationByConsId(Long consId, int page) {
        int totalCount = this.reservationMapper.countReservations(consId);
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = startRow + PAGE_SIZE - 1;
        List<ReservationResponseDto> reservations = this.reservationMapper.findReservationByConsId(consId, startRow, endRow);
        return new ReservationListRequestDto<>(reservations, page, totalPages, PAGE_SIZE, endRow);
    }

    @Override
    public ReservationResponseDto findReservationByRvtId(Long rvtId) {
        if (!this.reservationRepository.existsById(rvtId)) {
            throw new ReservationNotFoundException(rvtId, "예약을 찾을 수 없습니다.");
        }
        return this.reservationMapper.findReservationByRvtId(rvtId);
    }

    @Override
    public List<ReservationInfoResponseDto> getTotalCount() {
        return this.reservationMapper.getTotalCount();
    }
    
    @Override
    public List<ReservationInfoResponseDto> getTopFacilities() {
        return this.reservationMapper.getTopFacilities();
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
        LocalTime currentTime = openTime;
        
        while (currentTime.plusMinutes(unitTime).isBefore(closeTime) || currentTime.plusMinutes(unitTime).equals(closeTime)) {
            LocalTime nextTime = currentTime.plusMinutes(unitTime);
            RemainingCountResponseDto timeInfo = new RemainingCountResponseDto();
            timeInfo.setUsageStartTime(currentTime);

            int remainingCapacity = 0;

            // 현재 시간 슬롯에 해당하는 예약된 인원 확인
            for (HourlyUsageDto usage : reservedTimes) {
                if (usage.getUsageStartTime().isBefore(nextTime) && usage.getUsageEndTime().isAfter(currentTime)) {
                    remainingCapacity = 1;
                }
            }

            timeInfo.setRemainUsageCount(remainingCapacity);
            availableTimes.add(timeInfo);
            currentTime = nextTime;
        }
        return availableTimes;
    }

    @Override
    @Transactional
    public void saveReservation(ReservationNPaymentRequestDto dto) {
        Reservation reservationDto = dto.getReservation().toEntity();
        Payment paymentDto = dto.getPayment().toEntity();
        
        // 각 시간대별 예약된 인원 조회
        List<HourlyUsageDto> existingReservations = reservationMapper.getHourlyUsageCounts(dto.getReservation());
        Map<LocalTime, Integer> reservedCountMap = new HashMap<>();
        
        for (HourlyUsageDto hourUsage : existingReservations) {
            LocalTime startTime = hourUsage.getUsageStartTime();
            LocalTime endTime = hourUsage.getUsageEndTime();
            int reservedCount = hourUsage.getTotalUsageCount() > 0 ? 1 : 0;

            // 예약된 시간 동안 모든 단위 시간별 인원을 기록
            LocalTime time = startTime;
            while (time.isBefore(endTime)) {
                reservedCountMap.put(time, reservedCount);
                time = time.plusMinutes(dto.getReservation().getUnitUsageTime());
            }
        }

        // 새로 예약하려는 시간대별 인원 체크
        LocalTime checkTime = reservationDto.getUsageStartTime();
        while (checkTime.isBefore(reservationDto.getUsageEndTime())) {
            int currentReserved = reservedCountMap.getOrDefault(checkTime, 0);
            if (currentReserved > 0) {
                throw new ReservationDuplicatedException("예약 가능한 인원 수를 초과하였습니다.");
            }

            checkTime = checkTime.plusMinutes(dto.getReservation().getUnitUsageTime());
        }
        Reservation savedReservation = this.reservationRepository.saveAndFlush(reservationDto);
        this.paymentService.savePayment(savedReservation.getRvtId(), paymentDto);
        
        Long provIdOfFacility = this.reservationMapper.findProvIdByRvtId(savedReservation.getRvtId())
                .orElseThrow(NoSuchElementException::new);
        this.notificationService.sendMessage(provIdOfFacility, NotificationMessage.NEW_RESERVATION);
    }

    @Override
    @Transactional
    public void cancelReservation(Long rvtId, CancelRequestDto dto) {
        Reservation reservation = this.reservationRepository.findById(rvtId)
                .orElseThrow(() -> new ReservationNotFoundException(rvtId, "예약을 찾을 수 없습니다."));
        reservation.setRvtCancelationReason(dto.getRvtCancelationReason());
        this.paymentService.reservationCancelRequest(rvtId, dto);
    }

    @Override
    public Reservation findReservationEntityByRvtId(Long rvtId) {
        return this.reservationRepository.findById(rvtId).orElseThrow(() -> new ReservationNotFoundException(rvtId, "예약을 찾을 수 없습니다."));
    }

    @Override
    public List<FctReservationResponseDto> findReservationByFctId(Long fctId) {
        return this.reservationMapper.findReservationByFctId(fctId);
    }

}
