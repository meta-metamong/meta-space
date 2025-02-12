package com.metamong.mt.domain.reservation.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.metamong.mt.domain.notification.model.NotificationMessage;
import com.metamong.mt.domain.notification.service.NotificationService;
import com.metamong.mt.domain.reservation.dto.mapper.FindConsIdWithReservationTimeMapperDto;
import com.metamong.mt.domain.reservation.repository.mybatis.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final NotificationService notificationService;
    private final ReservationMapper reservationMapper;

    /**
     * <p>1시간마다 예약 알림을 전송한다.
     * 
     * <p>예약이 1시간 남은 사람과
     * 예약이 1일 남은 사람에게 예약 알림을 전송
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void sendReservationNotification() {
        LocalDateTime now = LocalDateTime.now();
        
        this.reservationMapper.findConsIdWithLeftReservationTime(
                new FindConsIdWithReservationTimeMapperDto(1, now)
        ).forEach((consId) -> this.notificationService.sendMessage(consId, NotificationMessage.RESERVATION_CHECK_ONE_HOUR_REMAIN));
        
        this.reservationMapper.findConsIdWithLeftReservationTime(
                new FindConsIdWithReservationTimeMapperDto(24, now)
        ).forEach((consId) -> this.notificationService.sendMessage(consId, NotificationMessage.RESERVATION_CHECK_ONE_HOUR_REMAIN));
    }
}
