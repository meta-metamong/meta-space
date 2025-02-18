package com.metamong.mt.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metamong.mt.domain.notification.service.NotificationService;
import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;
import com.metamong.mt.domain.payment.model.Payment;
import com.metamong.mt.domain.payment.service.PaymentService;
import com.metamong.mt.domain.reservation.dto.request.ReservationNPaymentRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.response.HourlyUsageDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.exception.ReservationDuplicatedException;
import com.metamong.mt.domain.reservation.model.Reservation;
import com.metamong.mt.domain.reservation.repository.jpa.ReservationRepository;
import com.metamong.mt.domain.reservation.repository.mybatis.ReservationMapper;
import com.metamong.mt.testutil.DummyEntityGenerator;

@ExtendWith(MockitoExtension.class)
public class DefaultReservaionServiceTest {

    @InjectMocks
    private DefaultReservationService reservationService;
    
    @Mock
    ReservationMapper reservationMapper;
    
    @Mock
    ReservationRepository reservationRepository;
    
    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Lock lock;

    @Test
    @DisplayName("예약 상세 목록 - find success")
    void findReservationByRvtId_findSuccess() throws Exception {
        // Given
    	Reservation reservation = DummyEntityGenerator.generateReservation(1L, 1L);
        
        ReservationResponseDto dto = new ReservationResponseDto();
		dto.setRvtId(reservation.getRvtId());
		dto.setRvtDate(reservation.getRvtDate());
		dto.setUsageStartTime(reservation.getUsageStartTime());
		dto.setUsageEndTime(reservation.getUsageEndTime());
		dto.setZoneName("Badminton Court");
		dto.setFctName("Badminton Hall");
		
		when(reservationRepository.existsById(1L)).thenReturn(true);
		when(reservationMapper.findReservationByRvtId(1L)).thenReturn(dto);
        
        // When
        ReservationResponseDto result = reservationService.findReservationByRvtId(1L);

        // Then
        assertThat(result.getRvtId()).isEqualTo(1L);
        assertThat(result.getRvtDate()).isEqualTo(LocalDate.of(2025, 2, 5));
        assertThat(result.getUsageStartTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(result.getUsageEndTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(result.getZoneName()).isEqualTo("Badminton Court");
        assertThat(result.getFctName()).isEqualTo("Badminton Hall");
    }
    
//    @Test
//    void 예약_저장_성공() throws Throwable {
//        // Given
//        ReservationRequestDto reservationRequest = new ReservationRequestDto(
//                1L, 2L, LocalDate.now().plusDays(1), 
//                LocalTime.of(10, 0), LocalTime.of(11, 0), 1
//        );
//
//        PaymentRequestDto paymentRequest = new PaymentRequestDto();
//        paymentRequest.setPayPrice(10000L);
//        paymentRequest.setPayMethod("CARD");
//
//        ReservationNPaymentRequestDto requestDto = new ReservationNPaymentRequestDto();
//        requestDto.setReservation(reservationRequest);
//        requestDto.setPayment(paymentRequest);
//
//        Reservation reservation = reservationRequest.toEntity();
//        Payment payment = paymentRequest.toEntity();
//
//        when(reservationMapper.getHourlyUsageCounts(any())).thenReturn(Collections.emptyList()); // 예약된 데이터 없음
//        when(reservationRepository.saveAndFlush(any(Reservation.class))).thenReturn(reservation);
//        when(reservationMapper.findProvIdByRvtId(anyLong())).thenReturn(Optional.of(1L));
//
//        doNothing().when(paymentService).savePayment(anyLong(), any(Payment.class));
//        doNothing().when(notificationService).sendMessage(anyLong(), any());
//
//        // When
//        assertThatCode(() -> reservationService.saveReservation(requestDto))
//                .doesNotThrowAnyException();
//
//        // Then
//        verify(reservationRepository, times(1)).saveAndFlush(any(Reservation.class));
//        verify(paymentService, times(1)).savePayment(anyLong(), any(Payment.class));
//        verify(notificationService, times(1)).sendMessage(anyLong(), any());
//    }

    
//    @Test
//    @DisplayName("예약이 정상적으로 저장되는지 확인")
//    void testSaveReservation_Success() {
//        // Given
//        ReservationNPaymentRequestDto dto = createValidReservationRequest();
//
//        // When
//        reservationService.saveReservation(dto);
//
//        // Then
//        Optional<Reservation> savedReservation = reservationRepository.findById(dto.getReservation().getRvtId());
//        assertThat(savedReservation).isPresent();
//    }

    @Test
    @DisplayName("동일한 시간대에 중복 예약이 발생하지 않아야 함")
    void testSaveReservation_Duplicate_Fail() throws InterruptedException {
        // Given
        ReservationNPaymentRequestDto dto = createValidReservationRequest();

        // 첫 번째 예약 성공
        when(reservationMapper.getHourlyUsageCounts(dto.getReservation()))
            .thenReturn(List.of(new HourlyUsageDto(LocalTime.of(10, 00), LocalTime.of(12, 00), 1)));

        // 두 번째 예약(같은 시간대) 시 예외 발생 검증
        assertThrows(ReservationDuplicatedException.class, () -> {
            reservationService.saveReservation(dto);
        });
    }
    
    @Test
    void 중복_예약_예외_발생() throws Throwable {
        // Given
        ReservationNPaymentRequestDto requestDto = createValidReservationRequest();

        // 이미 예약된 시간대가 존재하는 경우
        List<HourlyUsageDto> existingReservations = List.of(
                new HourlyUsageDto(LocalTime.of(10, 0), LocalTime.of(11, 0), 1)
        );

        when(reservationMapper.getHourlyUsageCounts(any())).thenReturn(existingReservations);

        // When & Then
        assertThatThrownBy(() -> reservationService.saveReservation(requestDto))
                .isInstanceOf(ReservationDuplicatedException.class)
                .hasMessage("예약 가능한 인원 수를 초과하였습니다.");
    }
    
//    @Test
//    @DisplayName("예약이 정상적으로 저장되는지 확인")
//    void testSaveReservation_Success() {
//        // Given
//        ReservationNPaymentRequestDto dto = createValidReservationRequest();
//        Reservation reservation = dto.getReservation().toEntity();
//        reservation.setRvtId(1L);
//        Payment payment = dto.getPayment().toEntity();
//
//        when(reservationMapper.getHourlyUsageCounts(any())).thenReturn(Collections.emptyList()); // 예약된 데이터 없음
//        when(reservationRepository.saveAndFlush(any(Reservation.class))).thenReturn(reservation);
//        when(reservationMapper.findProvIdByRvtId(anyLong())).thenReturn(Optional.of(1L));
//
//        // When
//        reservationService.saveReservation(dto);
//
//        // Then
//        verify(lock, times(1)).lock();
//        verify(lock, times(1)).unlock();
//        verify(reservationRepository, times(1)).saveAndFlush(any(Reservation.class));
//        verify(paymentService, times(1)).savePayment(anyLong(), any(Payment.class));
//        verify(notificationService, times(1)).sendMessage(anyLong(), any());
//    }
//
//    @Test
//    @DisplayName("중복 예약이 발생하면 예외가 발생해야 함")
//    void testSaveReservation_Duplicate_Fail() {
//        // Given
//        ReservationNPaymentRequestDto dto = createValidReservationRequest();
//        HourlyUsageDto existingUsage = new HourlyUsageDto(dto.getReservation().getUsageStartTime(), dto.getReservation().getUsageEndTime(), 1);
//
//        when(reservationMapper.getHourlyUsageCounts(any())).thenReturn(List.of(existingUsage));
//
//        // When & Then
//        assertThrows(ReservationDuplicatedException.class, () -> reservationService.saveReservation(dto));
//
//        verify(lock, times(1)).lock();
//        verify(lock, times(1)).unlock();
//        verify(reservationRepository, never()).saveAndFlush(any(Reservation.class));
//    }

    private ReservationNPaymentRequestDto createValidReservationRequest() {
        // 예약 DTO 생성
        ReservationRequestDto reservationDto = new ReservationRequestDto(
                1L,
                1L,
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                1,
                60
        );

        // 결제 DTO 생성
        PaymentRequestDto paymentDto = new PaymentRequestDto();
        paymentDto.setPayPrice(10000L);
        paymentDto.setPayMethod("CARD");

        return new ReservationNPaymentRequestDto(reservationDto, paymentDto);
    }
}

