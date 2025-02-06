package com.metamong.mt.domain.reservation.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
import com.metamong.mt.domain.reservation.model.Reservation;
import com.metamong.mt.domain.reservation.repository.jpa.ReservationRepository;
import com.metamong.mt.testutil.DummyEntityGenerator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ReservationMapperTest {

    @Mock
    ReservationMapper reservationMapper;

    @Autowired
    ReservationRepository reservationRepository;

//	@Transactional
//	@Test
//    @DisplayName("시설 이용자의 예약 목록 조회 - 성공")
//    void findReservationByConsId_successs() {
//        // Given
//        Reservation reservation = DummyEntityGenerator.generateReservation(1L, 1L);
//        this.reservationRepository.save(reservation);
//        
//        // When
//        List<ReservationResponseDto> result = this.reservationMapper.findReservationByConsId(1L);
//        
//        // Then
//        ReservationResponseDto rvt1 = result.get(0);
//        assertThat(rvt1.getRvtId()).isEqualTo(1L);
//        assertThat(rvt1.getRvtDate()).isEqualTo(LocalDate.of(2025, 2, 5));
//        assertThat(rvt1.getUsageStartTime()).isEqualTo(LocalTime.of(6, 0));
//        assertThat(rvt1.getUsageEndTime()).isEqualTo(LocalTime.of(9, 0));
//    }

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

        when(reservationMapper.findReservationByConsId(1L)).thenReturn(Arrays.asList(dto));

        // When
        List<ReservationResponseDto> result = reservationMapper.findReservationByConsId(1L);

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

}
