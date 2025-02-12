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
import com.metamong.mt.domain.notification.model.Notification;
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
                new Notification(this.sampleReceiver.getMemId(), "hi"),
                new Notification(this.sampleReceiver.getMemId(), "hi1"),
                new Notification(this.sampleReceiver.getMemId(), "hi2"),
                new Notification(this.sampleReceiver.getMemId(), "hi3"),
                new Notification(this.sampleReceiver.getMemId(), "hi4")
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
}
