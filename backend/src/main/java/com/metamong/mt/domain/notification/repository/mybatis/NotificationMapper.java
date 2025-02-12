package com.metamong.mt.domain.notification.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface NotificationMapper {
	
    void readByNotificationIds(@Param("readByNotificationIds") List<Long> notificationIds);
}
