package com.metamong.mt.domain.member.dto.response;

import java.time.LocalDate;

import com.metamong.mt.domain.member.model.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
	private final Long memberId;
	private final String email;
	private final String name;
	private final String phone;
	private final String gender;
	private final LocalDate birthDate;
	private final String postalCode;
	private final String address;
	private final String detailAddress;
    private final String businessName;
    private final String businessNumber;
    private final String bankCode;
    private final String account;
    private final String accountOwner;
    private final Role role;
    
}
