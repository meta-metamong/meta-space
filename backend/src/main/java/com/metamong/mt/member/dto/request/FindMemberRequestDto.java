package com.metamong.mt.member.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindMemberRequestDto {
	String idOrPw;
	@Nullable
	String userid;
	String name;
	String email;
}
