package com.metamong.mt.domain.notification.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.notification.dto.mapper.NotificationListMapperDto;
import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.model.NotificationMessage;
import com.metamong.mt.domain.notification.repository.jpa.NotificationRepository;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class NotificationMapperTest {
    
    @Autowired
    NotificationMapper notificationMapper;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    Member sampleReceiver;
    
    @BeforeEach
    void beforeEach() {
        this.sampleReceiver = Member.builder()
                .email("sample@gmail.com")
                .memName("hello")
                .password("1q2w3e4r")
                .memPhone("010-1234-1234")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1997, 9, 16))
                .memPostalCode("12345")
                .memDetailAddress("where i")
                .memAddress("hi")
                .role(Role.ROLE_CONS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDel(BooleanAlt.N)
                .build();
        this.sampleReceiver = this.memberRepository.save(this.sampleReceiver);
    }

    @Test
    @DisplayName("countNotReadNotificationsByReceiverId() - success")
    void countNotReadNotificationsByReceiverId_success() {
        // Given
        List<Notification> notifications = List.of(
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_ACCEPTED),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_REJECTED),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_ACCEPT),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.RESERVATION_CANCELATION),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_REJECTED)
        );
        
        this.notificationRepository.saveAll(notifications);
        
        this.notificationRepository.flush();
        
        // When
        this.notificationMapper.readByNotificationIds(
                notifications.stream().map(Notification::getNotiId).toList()
        );
        
        // Then
        assertThat(this.notificationRepository.countNotReadNotificationsByReceiverId(this.sampleReceiver.getMemId()))
                .isZero();
    }
    
    @Test
    @DisplayName("findNotificationsByReceiverId() - includeRead")
    void findNotificationsByReceiverId_includeRead() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = List.of(
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_ACCEPTED, now.plusHours(2), 'Y'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_REJECTED, now.plusHours(3), 'N'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_ACCEPT,  now.plusHours(1), 'N'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.RESERVATION_CANCELATION, now.plusHours(4), 'Y'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_REJECTED, now, 'N')
        );
        
        notifications.get(0).setIsRead('Y');
        notifications.get(3).setIsRead('Y');
        
        this.notificationRepository.saveAll(notifications);
        
        this.notificationRepository.flush();
        
        // When
        List<Notification> result = this.notificationMapper
                .findNotificationsByReceiverId(new NotificationListMapperDto(this.sampleReceiver.getMemId(), true));
        
        // Then
        Long[] expected = notifications.stream()
                .sorted((n1, n2) -> {
                    if (n1.getCreatedAt().isBefore(n2.getCreatedAt())) {
                        return 1;
                    } else if (n1.getCreatedAt().isAfter(n2.getCreatedAt())) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .map(Notification::getNotiId)
                .toArray(Long[]::new);
        assertThat(result).size().isEqualTo(expected.length);
        assertThat(result.stream().map(Notification::getNotiId))
                .containsExactly(expected);
    }
    
    @Test
    @DisplayName("findNotificationsByReceiverId() - notIncludeRead")
    void findNotificationsByReceiverId_notIncludeRead() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = List.of(
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_ACCEPTED, now.plusHours(2), 'Y'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_REJECTED, now.plusHours(3), 'N'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_ACCEPT,  now.plusHours(1), 'N'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.RESERVATION_CANCELATION, now.plusHours(4), 'Y'),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_REJECTED, now, 'N')
        );
        
        notifications.get(0).setIsRead('Y');
        notifications.get(3).setIsRead('Y');
        
        this.notificationRepository.saveAll(notifications);
        
        this.notificationRepository.flush();
        
        // When
        List<Notification> result = this.notificationMapper
                .findNotificationsByReceiverId(new NotificationListMapperDto(this.sampleReceiver.getMemId(), false));
        
        // Then
        Long[] expected = notifications.stream()
                .filter((e) -> e.getIsRead().equals('N'))
                .sorted((n1, n2) -> {
                    if (n1.getCreatedAt().isBefore(n2.getCreatedAt())) {
                        return 1;
                    } else if (n1.getCreatedAt().isAfter(n2.getCreatedAt())) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .map(Notification::getNotiId)
                .toArray(Long[]::new);
        assertThat(result).size().isEqualTo(expected.length);
        assertThat(result.stream().map(Notification::getNotiId))
                .containsExactly(expected);
    }
}
