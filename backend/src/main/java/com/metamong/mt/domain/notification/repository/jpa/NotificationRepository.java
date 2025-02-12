package com.metamong.mt.domain.notification.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
