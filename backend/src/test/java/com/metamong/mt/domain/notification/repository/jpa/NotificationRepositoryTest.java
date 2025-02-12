package com.metamong.mt.domain.notification.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.model.NotificationMessage;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    EntityManager entityManager;
    
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
        
        notifications.get(0).setIsRead('Y');
        notifications.get(3).setIsRead('Y');
        
        this.notificationRepository.saveAll(notifications);
        
        // When
        int result = this.notificationRepository.countNotReadNotificationsByReceiverId(this.sampleReceiver.getMemId());
        
        // Then
        assertThat(result).isEqualTo(3);
    }
    
    @Test
    @DisplayName("어떤 회원의 모든 알림 읽기 - success")
    void readAllNotificationsByReceiverId_success() {
        // Given
        List<Notification> notifications = List.of(
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_ACCEPTED),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.FACILITY_REGISTRATION_REJECTED),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_ACCEPT),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.RESERVATION_CANCELATION),
                new Notification(this.sampleReceiver.getMemId(), NotificationMessage.REFUND_REJECTED)
        );
        
        this.notificationRepository.saveAll(notifications);
        
        this.entityManager.flush();
        
        int unreadBefore = this.notificationRepository.countNotReadNotificationsByReceiverId(this.sampleReceiver.getMemId());
        assertThat(unreadBefore).isEqualTo(notifications.size());
        
        // When
        this.notificationRepository.readAllNotificationsByReceiverId(this.sampleReceiver.getMemId());;
        
        // Then
        int unreadAfter = this.notificationRepository.countNotReadNotificationsByReceiverId(this.sampleReceiver.getMemId());
        assertThat(unreadAfter).isZero();
    }
}
