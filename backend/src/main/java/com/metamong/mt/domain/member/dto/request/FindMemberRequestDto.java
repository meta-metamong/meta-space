package com.metamong.mt.domain.member.dto.request;

import com.metamong.mt.domain.member.model.constant.IdOrPw;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindMemberRequestDto {
	private IdOrPw idOrPw;
	@Nullable
	private String userId;
	private String name;
	private String email;
}
