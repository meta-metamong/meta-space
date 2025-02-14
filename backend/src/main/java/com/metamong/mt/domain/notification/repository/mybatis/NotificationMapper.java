package com.metamong.mt.domain.notification.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.notification.dto.mapper.NotificationListMapperDto;
import com.metamong.mt.domain.notification.model.Notification;

@Repository
@Mapper
public interface NotificationMapper {
    
    List<Notification> findNotificationsByReceiverId(@Param("dto") NotificationListMapperDto dto);
}
