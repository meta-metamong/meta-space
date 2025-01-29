package com.metamong.mt.member.dto.request;

import com.metamong.mt.member.model.IdOrPw;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindMemberRequestDto {
	private IdOrPw idOrPw;
	@Nullable
	private String userId;
	private String name;
	private String email;
}
