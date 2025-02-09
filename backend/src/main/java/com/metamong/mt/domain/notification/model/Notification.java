package com.metamong.mt.domain.notification.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification")  // 테이블 이름 매핑
@SequenceGenerator(
        name = "notification_seq",
        sequenceName = "seq_notification",
        initialValue = 1,
        allocationSize = 1
)
public class Notification {

    @Id    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    //@SequenceGenerator(name = "notification_seq", sequenceName = "SEQ_NOTIFICATION", allocationSize = 1)
    @Column(name = "noti_id")  // 'noti_id' 컬럼에 매핑
    private Long notiId;        // 알림 아이디

    @Column(name = "receiver_id", nullable = false)  // 'receiver_id' 컬럼에 매핑
    private Long receiverId;    // 수신자 아이디

    @Column(name = "noti_msg", nullable = false)  // 'noti_msg' 컬럼에 매핑
    private String notiMsg;     // 알림 메시지

    @Column(name = "created_at", nullable = false)  // 'created_at' 컬럼에 매핑
    private LocalDateTime createdAt;  // 등록 시간

    @Column(name = "is_read", nullable = false)  // 'is_read' 컬럼에 매핑
    private Boolean isRead;     // 알림 읽음 여부

    // 기본 생성자
    public Notification() {}

    // 생성자
    public Notification(Long receiverId, String notiMsg, LocalDateTime createdAt, Boolean isRead) {
        this.receiverId = receiverId;
        this.notiMsg = notiMsg;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Getter & Setter
    public Long getNotiId() {
        return notiId;
    }

    public void setNotiId(Long notiId) {
        this.notiId = notiId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getNotiMsg() {
        return notiMsg;
    }

    public void setNotiMsg(String notiMsg) {
        this.notiMsg = notiMsg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
