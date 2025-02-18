package com.metamong.mt.domain.reservation.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.response.HourlyUsageDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.model.Reservation;
import com.metamong.mt.domain.reservation.repository.jpa.ReservationRepository;
import com.metamong.mt.testutil.DummyEntityGenerator;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ReservationMapperMockTest {

    @Mock
    ReservationMapper reservationMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("시설 이용자의 예약 목록 조회 - 성공")
    void findReservationByConsId_success() {
        // Given
        Reservation reservation = DummyEntityGenerator.generateReservation(1L, 1L);

        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setRvtId(reservation.getRvtId());
        dto.setRvtDate(reservation.getRvtDate());
        dto.setUsageStartTime(reservation.getUsageStartTime());
        dto.setUsageEndTime(reservation.getUsageEndTime());
        dto.setZoneName("Badminton Court");
        dto.setFctName("Badminton Hall");

        when(reservationMapper.findReservationByConsId(1L, 1, 10)).thenReturn(Arrays.asList(dto));

        // When
        List<ReservationResponseDto> result = reservationMapper.findReservationByConsId(1L, 1, 10);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getRvtId()).isEqualTo(1L);
        assertThat(result.get(0).getRvtDate()).isEqualTo(LocalDate.of(2025, 2, 5));
        assertThat(result.get(0).getUsageStartTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(result.get(0).getUsageEndTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(result.get(0).getZoneName()).isEqualTo("Badminton Court");
        assertThat(result.get(0).getFctName()).isEqualTo("Badminton Hall");
    }

    @Test
    @DisplayName("예약 상세 조회 - 성공")
    void findReservationByRvtId_success() {
        // Given
        Reservation reservation = DummyEntityGenerator.generateReservation(1L, 1L);

        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setRvtId(reservation.getRvtId());
        dto.setRvtDate(reservation.getRvtDate());
        dto.setUsageStartTime(reservation.getUsageStartTime());
        dto.setUsageEndTime(reservation.getUsageEndTime());
        dto.setZoneName("Badminton Court");
        dto.setFctName("Badminton Hall");

        when(reservationMapper.findReservationByRvtId(1L)).thenReturn(dto);

        // When
        ReservationResponseDto result = reservationMapper.findReservationByRvtId(1L);

        // Then
        assertThat(result.getRvtId()).isEqualTo(1L);
        assertThat(result.getRvtDate()).isEqualTo(LocalDate.of(2025, 2, 5));
        assertThat(result.getUsageStartTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(result.getUsageEndTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(result.getZoneName()).isEqualTo("Badminton Court");
        assertThat(result.getFctName()).isEqualTo("Badminton Hall");
    }

    @Test
    @DisplayName("현재 예약된 시간 조회 - 성공")
    void getReservedTimes_success() {
        // Given
        ReservationRequestDto requestDto = new ReservationRequestDto(
                1L, 1L, LocalDate.of(2025, 2, 10), LocalTime.of(6, 0), LocalTime.of(9, 0), 1);  
        
        HourlyUsageDto responseDto = new HourlyUsageDto(LocalTime.of(6, 0), LocalTime.of(9, 0), 1);
        
        when(reservationMapper.getHourlyUsageCounts(requestDto))
                .thenReturn(Arrays.asList(responseDto));
        
        // When
        List<HourlyUsageDto> result = reservationMapper.getHourlyUsageCounts(requestDto);
        
        // Then
        assertThat(result.get(0).getTotalUsageCount()).isEqualTo(1);
        assertThat(result.get(0).getUsageStartTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(result.get(0).getUsageEndTime()).isEqualTo(LocalTime.of(9, 0));
    }

}
