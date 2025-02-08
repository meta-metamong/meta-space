package com.metamong.mt.domain.notification.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
	Long notiId; 
	Long receiverId;
	String notiMsg;
	LocalDateTime createdAt;
	char isRead;
}
