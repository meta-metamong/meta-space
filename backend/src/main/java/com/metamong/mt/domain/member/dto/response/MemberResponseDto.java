package com.metamong.mt.domain.member.dto.response;

import java.time.LocalDate;

import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
	private final Long memId;
	private final String email;
	private final String memName;
	private final String memPhone;
	private final Gender gender;
	private final LocalDate birthDate;
	private final String memPostalCode;
	private final String memAddress;
	private final String memDetailAddress;
	private final Role role;

	private final String bizName;
  private final String bizRegNum;
  private final String bankCode;
  private final String provAccount;
  private final String provAccountOwner;
    
}
