package com.metamong.mt.domain.notification.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.metamong.mt.domain.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    @Query("""
            SELECT COUNT(*)
            FROM Notification n
            WHERE n.receiverId = :receiverId
                AND n.isRead = 'N'
            """)
    int countNotReadNotificationsByReceiverId(@Param("receiverId") Long receiverId);
}
