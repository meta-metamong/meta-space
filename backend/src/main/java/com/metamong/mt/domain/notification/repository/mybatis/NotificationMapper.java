package com.metamong.mt.domain.notification.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.notification.model.Notification;

@Repository
@Mapper
public interface NotificationMapper {
	void insertNotification(Notification notification);

}
