package com.metamong.mt.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;
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
}

