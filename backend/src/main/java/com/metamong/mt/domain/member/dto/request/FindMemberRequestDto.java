package com.metamong.mt.domain.member.dto.request;

import com.metamong.mt.domain.member.model.constant.IdOrPw;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FindMemberRequestDto {
	private IdOrPw idOrPw;
	@Nullable
	private String userId;
	private String name;
	private String email;
}
